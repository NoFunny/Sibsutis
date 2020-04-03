package compiler.identifierTable;

import compiler.identifierTable.SemanticExceptioin.SemanticException;
import compiler.parser.AST.ArrayMember.ArrayMember;
import compiler.parser.AST.ArrayMember.ArrayMemberId;
import compiler.parser.AST.Else.NodeJustElse;
import compiler.parser.AST.Inits.*;
import compiler.parser.AST.Node;
import compiler.parser.AST.NodeArgsInit;
import compiler.parser.AST.NodeClass;
import compiler.parser.AST.NodeMainMethod;
import compiler.parser.AST.statement.*;
import compiler.parser.AST.value.Attachment;
import compiler.parser.AST.value.CallArrayMember;
import compiler.parser.AST.value.FuncCall;
import compiler.parser.AST.value.GenericValue;

import java.util.*;

public class Table implements GenericUnit {
    private String nameTable;
    private Map<String,GenericUnit> mainTable = new HashMap<>();
    private Table parentTable = null;
    private NodeStatement node;

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public void setParentTable(Table parentTable) {
        this.parentTable = parentTable;
    }

    public void setNode(NodeStatement node) {
        this.node = node;
    }

    public Map<String, GenericUnit> getMainTable() {
        return mainTable;
    }

    public NodeStatement getNode() {
        return node;
    }

    public void go(NodeClass root) throws SemanticException {
        nameTable = "GLOBAL_TABLE";
        next(root);
    }

    private void next(Node node) throws SemanticException {
        if (node instanceof NodeClass) {
            for (NodeInit nodeInit : ((NodeClass) node).getInitList()) {
                addInitNode(nodeInit);
            }

            Table newTable = new Table();
            newTable.setNameTable("MAIN_METHOD");
            newTable.setParentTable(this);
            newTable.next(((NodeClass) node).getMainMethod());
            mainTable.put("MainMethod", newTable);
        } else if (node instanceof CallFunc) {
            for (NodeArgsInit nodeArgsInit : ((CallFunc) node).getNodeArgsInitList()) {
                addArgInitNode(nodeArgsInit);
            }
            for (NodeStatement statement : ((CallFunc) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }
        } else if (node instanceof NodeConditional) {
            for (NodeStatement statement : ((NodeConditional) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }

            if (((NodeConditional) node).getElseNode() != null) {
                next(((NodeConditional) node).getElseNode());
            }
        } else if (node instanceof NodeLoop) {
            for (NodeStatement statement : ((NodeLoop) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }
        } else if (node instanceof NodeJustElse) {
            for (NodeStatement statement : ((NodeJustElse) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }
        } else if (node instanceof NodeMainMethod) {
            for (NodeStatement statement : ((NodeMainMethod) node).getStatementList()) {
                if (statement != null) {
                    addStatement(statement);
                }
            }
        }
    }

    private void addInitNode(NodeInit nodeInit) throws SemanticException {
        ForkInit tmpForkInit = nodeInit.getForkInit();

        if (tmpForkInit instanceof ForkInitVar ||
                tmpForkInit instanceof ForkInitArray) {
            String tmpNameID = nodeInit.getId().getValue();

            if (!containsKey(tmpNameID)) {
                mainTable.put(tmpNameID, new Id(nodeInit));
            } else {
                throw new SemanticException(String.format("%s already init in table <%s>",
                        tmpNameID,
                        nameTable));
            }

            if (tmpForkInit instanceof ForkInitVar) {
                if (!checkExpression(((ForkInitVar) tmpForkInit).getExpression())) {
                    throw new SemanticException(String.format("%s not init in table <%s>",
                            tmpForkInit,
                            nameTable));
                }
            }
        } else if (tmpForkInit instanceof CallFunc) {
            String tmpNameID = nodeInit.getId().getValue();

            if (!containsKey(tmpNameID)) {
                Table newTable = new Table();
                newTable.setNode(nodeInit);
                newTable.setNameTable(tmpNameID);
                newTable.setParentTable(this);
                newTable.next(tmpForkInit);
                mainTable.put(tmpNameID, newTable);
            } else {
                throw new SemanticException(String.format("%s already init in table <%s>",
                        tmpNameID,
                        nameTable));
            }
        }
    }

    private boolean checkExpression(NodeStatement statement) throws SemanticException {
        boolean result = false;
        NodeExpression expr = null;

        if (statement instanceof NodeLoop) {
            expr = ((NodeLoop) statement).getExpression();
        } else if (statement instanceof NodeConditional) {
            expr = ((NodeConditional) statement).getExpression();
        } else if (statement instanceof NodeReturn) {
            expr = ((NodeReturn) statement).getExpression();
        } else if (statement instanceof NodePrintln) {
            expr = ((NodePrintln) statement).getExpression();
        }else if (statement instanceof NodeExpression) {
            expr = (NodeExpression) statement;
        }

        if (expr != null) {
            GenericValue gv;

            if ((gv = expr.getlValue()) != null) {
                System.out.println(expr.getlValue().getValue());
                result = checkGenericValue(gv);
            }
            if ((gv = expr.getrValue()) != null) {
                result = checkGenericValue(gv);
            }


//Прописать условие для lvalue
            NodeExpression tmpExpr;
            System.out.println(expr.getlExpression());
            if ((tmpExpr = expr.getlExpression()) != null) {
                System.out.println(tmpExpr);
                result = checkExpression(tmpExpr);
            }
            System.out.println(expr.getrExpression());
            if ((tmpExpr = expr.getrExpression()) != null) {
                System.out.println(tmpExpr);
                result = checkExpression(tmpExpr);
            }
        }
        return result;
    }

    private boolean checkGenericValue(GenericValue gv) throws SemanticException {
        boolean result = false;

        if (!gv.getValue().getType().equals("STRING") &&
                (!gv.getValue().getType().equals("INTEGER") ||
                        !gv.getValue().getType().equals("OCTAL") ||
                        !gv.getValue().getType().equals("HEX") ||
                        !gv.getValue().getType().equals("FLOAT"))) {
            result = containsKey(gv.getValue().getValue());
        }

        if (!result) {
            return true;
        }

        if (gv instanceof Attachment) {
            result = checkExpression(((Attachment) gv).getExpression());
        } else if (gv instanceof CallArrayMember) {
            ArrayMember am = ((CallArrayMember) gv).getArrayMember();

            if (am instanceof ArrayMemberId) {
                result = containsKey(((ArrayMemberId) am).getId().getValue());
            }
        } else if (gv instanceof FuncCall) {
            GenericUnit gu = findIdByName(gv.getValue().getValue());
            NodeStatement node = null;
            List<NodeArgsInit> argsInitList = null;

            if (gu != null) {
                if (gu instanceof Table) {
                    node = ((Table) gu).getNode();
                }
            }

            if (node != null) {
                if (node instanceof NodeInit) {
                    ForkInit tmpForkInit = ((NodeInit) node).getForkInit();

                    if (tmpForkInit instanceof CallFunc) {
                        argsInitList = ((CallFunc) tmpForkInit).getNodeArgsInitList();
                    }
                }
            }
            if (argsInitList != null) {
                if (argsInitList.size() != ((FuncCall) gv).getArgsCall().size()) {
                    throw new SemanticException(String.format("ERROR semantic: invalid count args <%s> in %s",
                            gv.getValue(),
                            nameTable));
                }
            }

            for (int i = 0; i < ((FuncCall) gv).getArgsCall().size(); i++) {
                GenericUnit dataType = findIdByName(((FuncCall) gv).getArgsCall().get(i).getValue().getValue());
                if (!argsInitList.get(i).getDataType().getValue()
                        .equals(((Id) dataType).getNode().getDataType().getValue()))
                {
                    throw new SemanticException(String.format("ERROR semantic: invalid args <%s> in %s",
                            gv.getValue(),
                            nameTable));
                }

                result = checkGenericValue(((FuncCall) gv).getArgsCall().get(i));
            }
        } else if (gv != null) {
            result = true;
        }

        return result;
    }

    private GenericUnit findIdByName(String name) {
        GenericUnit gu = null;
        for (String key : mainTable.keySet()) {
            if (key.equals(name)) {
                return mainTable.get(key);
            }
        }

        if (parentTable != null) {
            gu = parentTable.findIdByName(name);
        }

        return gu;
    }

    private void addArgInitNode(NodeArgsInit nodeArgsInit) throws SemanticException {
        String tmpNameID = nodeArgsInit.getId().getValue();
        if (!containsKey(tmpNameID)) {
            mainTable.put(tmpNameID, new ArgId(nodeArgsInit));
        } else {
            throw new SemanticException(String.format("%s already init in talbe <%s>",
                    tmpNameID,
                    nameTable));
        }
    }

    private boolean containsKey(String tmpNameID) {
        for (String key : mainTable.keySet()) {
            if (key.equals(tmpNameID)) {
                return true;
            }
        }

        boolean result = false;

        if (parentTable != null) {
            result = parentTable.containsKey(tmpNameID);
        }

        return result;
    }

    private void addStatement(NodeStatement statement) throws SemanticException {
        if (statement instanceof NodeInit) {
            addInitNode((NodeInit) statement);
        } else {
            if (statement instanceof NodeConditional ||
                    statement instanceof NodeLoop) {
                Table newTable = new Table();
                newTable.setNameTable(statement.toString()
                        .split("@")[0]);
                newTable.setParentTable(this);
                newTable.next(statement);
                mainTable.put(statement.toString(), newTable);
            }

            if (statement instanceof NodeScanln) {
                String id = ((NodeScanln) statement).getId().getValue();
                if (!containsKey(id)) {
                    throw new SemanticException(String.format("%s not init in table <%s>",
                            id,
                            nameTable));
                }
            } else if (!checkExpression(statement)) {
                throw new SemanticException(String.format("%s not init in table <%s>",
                        statement,
                        nameTable));
            }
        }
    }

    public void printTable() { printTable(this, "GLOBAL_TABLE"); }

    private void printTable(Table table, String nameTable) {
        System.out.println(String.format("\n\tTable %s:",
                nameTable));

        Set<String> table_keys = new TreeSet<>();

        for (String key : table.getMainTable().keySet()) {
            if (table.getMainTable().get(key) instanceof Id ||
                    table.getMainTable().get(key) instanceof ArgId) {
                System.out.println(String.format("%s %s",
                        key,
                        table.getMainTable().get(key)));
            } else if (table.getMainTable().get(key) instanceof Table) {
                System.out.println(String.format("%s %s",
                        key,
                        (table.getMainTable().get(key))));
                table_keys.add(key);
            }
        }

        for (String table_id : table_keys) {
            printTable((Table) table.getMainTable().get(table_id), table_id);
        }
    }
}

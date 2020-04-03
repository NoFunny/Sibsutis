package compiler.parser;

import compiler.lexer.LexerList;
import compiler.lexer.Token;
import compiler.parser.AST.ArrayMember.ArrayMember;
import compiler.parser.AST.ArrayMember.ArrayMemberId;
import compiler.parser.AST.ArrayMember.ArrayMemberNumber;
import compiler.parser.AST.Else.NodeElse;
import compiler.parser.AST.Else.NodeIfElse;
import compiler.parser.AST.Else.NodeJustElse;
import compiler.parser.AST.Inits.*;
import compiler.parser.AST.NodeArgsInit;
import compiler.parser.AST.NodeClass;
import compiler.parser.AST.NodeMainMethod;
import compiler.parser.AST.TypeInit;
import compiler.parser.AST.operator.Operator;
import compiler.parser.AST.statement.*;
import compiler.parser.AST.value.*;
import compiler.parser.AST.value.Number;
import compiler.parser.ParseException.CriticalParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private LexerList lexerList;
    private NodeClass root = null;

    public Parser(LexerList lexerList) {
        this.lexerList = lexerList;
    }

    public void showTree() {
        int startIndex = 0;
        System.out.println(root.wrapperVisit(startIndex));
    }

    public void printTreeToFile() {
        int startIndex = 0;
        String filename = "AST.graph";

        try (FileWriter fw = new FileWriter(filename, false)) {
            fw.write(root.wrapperVisit(startIndex));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NodeClass go() {
        try {
            lexerList.match("program");
            startParse();
            return root;
        }catch (CriticalParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void startParse() throws CriticalParseException {
        root = parseClass();
    }

    private NodeClass parseClass() throws CriticalParseException {
        NodeClass node = new NodeClass();

        node.setModAccessToken(lexerList.getLookahead());
        parseModAccessClass();

        lexerList.match("CLASS");

        node.setIdToken(lexerList.getLookahead());
        lexerList.match("ID");

        lexerList.match("OPEN_CURL_BRACKET");

        List<NodeInit> initList = new ArrayList<>();
        parseInitList(initList);
        for(NodeInit init : initList) {
            node.addInit(init);
        }

        node.setMainMethod(parseMainMethod());

        lexerList.match("CLOSE_CURL_BRACKET");

        return node;
    }



    private void parseModAccessClass() throws CriticalParseException{
        lexerList.matchOneOf("PUBLIC", "PRIVATE", "PROTECTED");
    }

    private void parseInitList(List<NodeInit> initList) throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();
        if (currentTokenType.equals("INT") ||
                currentTokenType.equals("FLOAT") ||
                currentTokenType.equals("FLOOAT") ||
                currentTokenType.equals("CHAR")) {
            initList.add(parseInitInsideClass());
            parseInitList(initList);
        }
    }

    private NodeInit parseInitInsideClass() throws CriticalParseException {
        NodeInit node = new NodeInit();

        node.setDataType(lexerList.getLookahead());
        parseNativeDataType();

        List<Token> id = new ArrayList<>(1);
        node.setForkInit(parseFirstForkInitInsideClass(id));
        node.setId(id.get(0));

        return node;
    }

    private ForkInit parseFirstForkInitInsideClass(List<Token> id) throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        ForkInit forkInit;

        if (currentTokenType.equals("OPEN_SQUARE_BRACKET")) {
            lexerList.match("OPEN_SQUARE_BRACKET");
            lexerList.match("CLOSE_SQUARE_BRACKET");
            id.add(lexerList.getLookahead());
            lexerList.match("ID");
            forkInit = parseForkInitArray();
        } else if (currentTokenType.equals("ID")) {
            id.add(lexerList.getLookahead());
            lexerList.match("ID");
            forkInit = parseSecondForkInitInsideClass();
        } else {
            throw new CriticalParseException("expecting <" + "OPEN_SQUARE_BRACKET or ID"
                    + ">, but found is <"+ lexerList.getLookahead().getType() +
                    ":" + lexerList.getLookahead().getValue()
                    + "> in " + lexerList.getLookahead().getLocation());
        }

        forkInit.chooseType();

        return forkInit;

    }

    private ForkInit parseForkInitArray() throws CriticalParseException {
        ForkInitArray node = new ForkInitArray();

        lexerList.match("EQUAL");
        lexerList.match("NEW");

        node.setDataType(lexerList.getLookahead());
        parseNativeDataType();

        node.setIndex(parseArrayMember());

        lexerList.match("SEMICOLON");

        return node;
    }

    private ArrayMember parseArrayMember() throws CriticalParseException {
        lexerList.match("OPEN_SQUARE_BRACKET");
        ArrayMember node = parseArrayMemberFork();
        lexerList.match("CLOSE_SQUARE_BRACKET");

        return node;
    }

    private ArrayMember parseArrayMemberFork() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        ArrayMember arrayMember = null;

        if (currentTokenType.equals("INTEGER") ||
                currentTokenType.equals("INT") ||
                currentTokenType.equals("OCTAL") ||
                currentTokenType.equals("HEX") ||
                currentTokenType.equals("FLOAT") ||
                currentTokenType.equals("FLOOAT") ||
                currentTokenType.equals("BINARSUB")) {
            arrayMember = new ArrayMemberNumber(parseNumber());
        } else if (currentTokenType.equals("ID")) {
            arrayMember = new ArrayMemberId(lexerList.getLookahead());
            lexerList.match("ID");
        } else {
            throw new CriticalParseException("expecting <ID or INT,FLOAT,OCTAL,HEX"
                    + ">, but found is <"+ lexerList.getLookahead().getType() +
                    ":" + lexerList.getLookahead().getValue()
                    + "> in " + lexerList.getLookahead().getLocation());
        }

        return arrayMember;
    }

    private ForkInit parseSecondForkInitInsideClass() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        ForkInit forkInit;

        if (currentTokenType.equals("OPEN_BRACKET")) {
            forkInit = parseForkInitFunc();
        } else if (currentTokenType.equals("EQUAL")) {
            forkInit = parseForkInitVar();
        } else {
            throw new CriticalParseException("expecting <" + "OPEN_BRACKET or EQUAL"
                    + ">, but found is <"+ lexerList.getLookahead().getType() +
                    ":" + lexerList.getLookahead().getValue()
                    + "> in " + lexerList.getLookahead().getLocation());
        }

        return forkInit;
    }

    private ForkInit parseForkInitFunc() throws CriticalParseException {
        CallFunc node = new CallFunc();

        lexerList.match("OPEN_BRACKET");

        List<NodeArgsInit> listArgs = parseArgsInitListChanger();
        if (listArgs != null) {
            for (NodeArgsInit arg : listArgs) {
                node.addArgInit(arg);
            }
        }

        lexerList.match("CLOSE_BRACKET");

        lexerList.match("OPEN_CURL_BRACKET");

        List<NodeStatement> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (NodeStatement statement : statementList) {
            node.addStatement(statement);
        }

        lexerList.match("CLOSE_CURL_BRACKET");

        return node;
    }

    private void parseNativeDataType() throws CriticalParseException {
        lexerList.matchOneOf("INTEGER", "INT", "CHAR", "FLOAT", "FLOOAT", "OCTAL", "HEX");
    }

    private ForkInit parseForkInitVar() throws CriticalParseException {
        ForkInitVar node = new ForkInitVar();

        lexerList.match("EQUAL");
        node.setExpression(parseExpression());

        return node;
    }

    private NodeStatement parseStatement() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();
        NodeStatement node = null;

        if (currentTokenType.equals("OPEN_CURL_BRACKET")) {
            lexerList.match("OPEN_CURL_BRACKET");
            node = parseStatement();
            lexerList.match("OPEN_CURL_BRACKET");
        } else if (currentTokenType.equals("WHILE_OPERATOR")) {
            node = parseLoop();
        } else if (currentTokenType.equals("IF_OPERATOR")) {
            node = parseConditional();
        } else if (currentTokenType.equals("ID") ||
                currentTokenType.equals("INTEGER") ||
                currentTokenType.equals("FLOAT") ||
                currentTokenType.equals("OCTAL") ||
                currentTokenType.equals("HEX") ||
                currentTokenType.equals("BINARSUB") ||
                currentTokenType.equals("STRING")) {
            node = parseExpression();
        } else if (currentTokenType.equals("INT") ||
                currentTokenType.equals("FLOOAT") ||
                currentTokenType.equals("CHAAR")) {
            node = parseInitInsideFunc();
        } else if (currentTokenType.equals("SEMICOLON")) {
            lexerList.match("SEMICOLON");
        } else if (currentTokenType.equals("RETURN")) {
            node = parseReturn();
        } else if (currentTokenType.equals("PRINTLN")) {
            node = parsePrintln();
        } else if (currentTokenType.equals("SCANLN")) {
            node = parseScanln();   
        }
        return node;
    }

    private NodeStatement parseScanln() throws CriticalParseException {
        NodeScanln node = new NodeScanln();

        lexerList.match("SCANLN");
        lexerList.match("OPEN_BRACKET");

        node.setId(lexerList.getLookahead());
        lexerList.match("ID");

        lexerList.match("CLOSE_BRACKET");
        lexerList.match("SEMICOLON");

        return node;
    }

    private NodeStatement parseInitInsideFunc() throws CriticalParseException {
        NodeInit node = new NodeInit();
        node.setDataType(lexerList.getLookahead());
        parseNativeDataType();

        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("OPEN_SQUARE_BRACKET")) {
            lexerList.match("OPEN_SQUARE_BRACKET");
            lexerList.match("CLOSE_SQUARE_BRACKET");

            node.setId(lexerList.getLookahead());
            lexerList.match("ID");

            ForkInit forkInit = parseForkInitArray();
            forkInit.chooseType();
            node.setForkInit(forkInit);
        } else if (currentTokenType.equals("ID")) {
            node.setId(lexerList.getLookahead());
            lexerList.match("ID");

            ForkInit forkInit = parseForkInitVar();
            forkInit.chooseType();
            node.setForkInit(forkInit);
        } else {
            throw new CriticalParseException("expecting <" + "OPEN_SQUARE_BRACKET or ID"
                    + ">, but found is <"+ lexerList.getLookahead().getType() +
                    ":" + lexerList.getLookahead().getValue()
                    + "> in " + lexerList.getLookahead().getLocation());
        }

        return node;
    }

    private NodeStatement parsePrintln() throws CriticalParseException {
        NodePrintln node = new NodePrintln();

        lexerList.match("PRINTLN");
        lexerList.match("OPEN_BRACKET");
        node.setExpression(parseExpression());
        lexerList.match("CLOSE_BRACKET");
        lexerList.match("SEMICOLON");

        return node;
    }

    private NodeExpression parseExpression() throws CriticalParseException {
        NodeExpression node = new NodeExpression();
        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("ID") ||
                currentTokenType.equals("INT") ||
                currentTokenType.equals("INTEGER") ||
                currentTokenType.equals("OCTAL") ||
                currentTokenType.equals("HEX") ||
                currentTokenType.equals("FLOOAT") ||
                currentTokenType.equals("FLOAT") ||
                currentTokenType.equals("STRING") ||
                currentTokenType.equals("BINARSUB") ||
                currentTokenType.equals("OPEN_BRACKET")) {
            parseArithmetic(node);
            parseExprFork(node);
        } else {
            throw new CriticalParseException("expecting <ID or INTEGER,FLOAT,HEX,OCTAL or BINARSUB" +
                    " or STRING or OPEN_BRACKET>, but found is <"
                    + lexerList.getLookahead().getType()
                    + ":" + lexerList.getLookahead().getValue()
                    + "> in " + lexerList.getLookahead().getLocation());
        }

        return node;

    }

    private void parseExprFork(NodeExpression node) throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("LOGIC_AND") ||
                currentTokenType.equals("LOGIC_OR")) {
            node.setOperator(parseLogicOperator());
            node.setrExpression(parseExpression());
        } else if (currentTokenType.equals("LESS") ||
                currentTokenType.equals("GREATER") ||
                currentTokenType.equals("EqEQUAL") ||
                currentTokenType.equals("NotEqual") ||
                currentTokenType.equals("EQUAL")) {
            node.setOperator(parseConditions());
            node.setrExpression(parseExpression());
        } else if (currentTokenType.equals("BINARSUB") ||
                currentTokenType.equals("BINARPLUS") ||
                currentTokenType.equals("MUL") ||
                currentTokenType.equals("DIV")) {
            node.setOperator(parseOperator());
        } else if (currentTokenType.equals("SEMICOLON")) {
            try {
                lexerList.match("SEMICOLON");
            } catch (CriticalParseException e) {
                e.printStackTrace();
            }
        }
    }

    private Operator parseLogicOperator() throws CriticalParseException {
        Operator node = new Operator(lexerList.getLookahead());
        lexerList.matchOneOf("LOGIC_AND", "LOGIC_OR");
        return node;
    }

    private Operator parseOperator() throws CriticalParseException {
        Operator operator = new Operator(lexerList.getLookahead());
        lexerList.matchOneOf("BINARPLUS", "BINARSUB", "MUL", "DIV", "EQUAL");

        return operator;
    }

    private void parseArithmetic(NodeExpression node) throws CriticalParseException {
        parseSecondPrior(node);
    }

    private void parseSecondPrior(NodeExpression node) throws CriticalParseException {
        parseFirstPrior(node);
        parseSecondPrior_(node);
    }

    private void parseSecondPrior_(NodeExpression node) throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("BINARPLUS") || currentTokenType.equals("BINARSUB")) {
            parseSecondPriorOper(node);
            parseFirstPrior(node);
            parseSecondPrior_(node);
        }
    }

    private void parseFirstPrior(NodeExpression node) throws CriticalParseException {
        parseGroup(node);
        parseFirstPrior_(node);
    }

    private void parseFirstPrior_(NodeExpression node) throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("MUL") || currentTokenType.equals("DIV")) {
            parseFirstPriorOper(node);
            parseGroup(node);
            parseFirstPrior_(node);
        }
    }

    private void parseSecondPriorOper(NodeExpression node) throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (node.getOperator() == null) {
            node.setOperator(new Operator(lexerList.getLookahead()));
        } else {
            if (node.getrValue() == null && node.getrExpression() != null) {
                parseFirstPriorOper(node.getrExpression());
            } else if (node.getrValue() != null) {
                NodeExpression newNode = new NodeExpression();
                newNode.setOperator(new Operator(lexerList.getLookahead()));
                newNode.setlValue(node.getrValue());
                node.setrValue(null);
                node.setrExpression(newNode);
            } else if (node.getrExpression() == null && node.getrValue() == null) {
                NodeExpression newNode = new NodeExpression();
                parseFirstPriorOper(newNode);
            }
        }

        if (currentTokenType.equals("BINARPLUS")) {
            lexerList.match("BINARPLUS");
        } else if (currentTokenType.equals("BINARSUB")) {
            lexerList.match("BINARSUB");
        } else if (currentTokenType.equals("EQUAL")) {
            lexerList.match("EQUAL");
        } else {
            throw new CriticalParseException("expecting <" + "BINARPLUS or BINARSUB"
                    + ">, but found is <"+ lexerList.getLookahead().getType() +
                    ":" + lexerList.getLookahead().getValue()
                    + "> in " + lexerList.getLookahead().getLocation());
        }
    }

    private void parseFirstPriorOper(NodeExpression node) throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (node.getOperator() == null) {
            node.setOperator(new Operator(lexerList.getLookahead()));
        } else {
            if (node.getrValue() == null && node.getrExpression() != null) {
                parseFirstPriorOper(node.getrExpression());
            } else if (node.getrValue() != null) {
                NodeExpression newNode = new NodeExpression();
                newNode.setOperator(new Operator(lexerList.getLookahead()));
                newNode.setlValue(node.getrValue());
                node.setrValue(null);
                node.setrExpression(newNode);
            } else if (node.getrExpression() == null && node.getrValue() == null) {
                NodeExpression newNode = new NodeExpression();
                parseFirstPriorOper(newNode);
            }
        }

        if (currentTokenType.equals("MUL")) {
            lexerList.match("MUL");
        } else if (currentTokenType.equals("DIV")) {
            lexerList.match("DIV");
        } else {
            throw new CriticalParseException("expecting <" + "MUL or DIV"
                    + ">, but found is <"+ lexerList.getLookahead().getType() +
                    ":" + lexerList.getLookahead().getValue()
                    + "> in " + lexerList.getLookahead().getLocation());
        }
    }


    private void parseGroup(NodeExpression node) throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("OPEN_BRACKET")) {
            lexerList.match("OPEN_BRACKET");
            NodeExpression expression = new NodeExpression();
            parseArithmetic(expression);

            if (node.getlExpression() == null) {
                node.setlExpression(expression);
            } else if (node.getrExpression() == null) {
                node.setrExpression(expression);
            } else {
                System.out.println("ERROR in parseGroup");
            }

            lexerList.match("CLOSE_BRACKET");
            parseExprFork(node);
        } else if (currentTokenType.equals("ID") ||
                currentTokenType.equals("INT") ||
                currentTokenType.equals("INTEGER") ||
                currentTokenType.equals("FLOOAT") ||
                currentTokenType.equals("FLOAT") ||
                currentTokenType.equals("OCTAL") ||
                currentTokenType.equals("HEX") ||
                currentTokenType.equals("BINARSUB") ||
                currentTokenType.equals("STRING")) {
            if (node.getlValue() == null) {
                node.setlValue(parseValueExpr());
            } else if (node.getrValue() == null) {
                node.setrValue(parseValueExpr());
            } else {
                if (node.getrExpression() != null) {
                    parseGroup(node.getrExpression());
                } else {
                    System.out.println("ERROR in parseGroup");
                }
            }
        } else {
            throw new CriticalParseException("expecting <" + "OPEN_BRACKET or ID or INTEGER,FLOAT,HEX,OCTAL or STRING"
                    + ">, but found is <"+ lexerList.getLookahead().getType() +
                    ":" + lexerList.getLookahead().getValue()
                    + "> in " + lexerList.getLookahead().getLocation());
        }
    }

    private GenericValue parseValueExpr() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();
        GenericValue node = null;
        if (currentTokenType.equals("ID")) {
            node = parseVExpr();
        } else if (currentTokenType.equals("INT") ||
                    currentTokenType.equals("INTEGER") ||
                    currentTokenType.equals("FLOAT") ||
                    currentTokenType.equals("FLOOAT") ||
                    currentTokenType.equals("OCTAL") ||
                    currentTokenType.equals("HEX") ||
                currentTokenType.equals("BINARSUB")) {
            node = parseNumber();
        } else if (currentTokenType.equals("STRING")) {
            node = new StrLiteral();
            node.setValue(lexerList.getLookahead());
            lexerList.match("STRING");
        } else {
            throw new CriticalParseException("expecting <" + "ID or INTEGER,OCTAL,FLOAT,HEX or STRING"
                    + ">, but found is <"+ lexerList.getLookahead().getType() +
                    ":" + lexerList.getLookahead().getValue()
                    + "> in " + lexerList.getLookahead().getLocation());
        }

        return node;
    }

    private Number parseNumber() throws CriticalParseException {
        Number node = new Number();

        boolean isNegativeNumber = parseSign();
        node.setNegative(isNegativeNumber);

        node.setValue(lexerList.getLookahead());
        lexerList.matchOneOf("INT" ,"INTEGER", "OCTAL", "HEX", "FLOAT", "FLOOAT");

        return node;
    }

    private boolean parseSign() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("BINARSUB")) {
            lexerList.match("BINARSUB");
            return true;
        }

        return false;
    }

    private GenericValue parseVExpr() throws CriticalParseException {
        Token savingTokenID = lexerList.getLookahead();
        lexerList.match("ID");

        GenericValue node = parseVExprChanger();
        if (node != null) {
            node.setValue(savingTokenID);
        } else {
            node = new GenericValue();
            node.setValue(savingTokenID);
        }

        return node;

    }

    private GenericValue parseVExprChanger() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if(currentTokenType.equals("OPEN_SQUARE_BRACKET")) {
            CallArrayMember node = new CallArrayMember();
            node.setArrayMember(parseArrayMember());
            return node;
        } else if (currentTokenType.equals("OPEN_BRACKET")) {
            lexerList.match("OPEN_BRACKET");
            FuncCall node = new FuncCall();
            node.setArgsCall(parseArgsCallListChanger());
            lexerList.match("CLOSE_BRACKET");
            return node;
        } else if (currentTokenType.equals("EQUAL")) {
            lexerList.match("EQUAL");
            Attachment node = new Attachment();
            node.setExpression(parseExpression());
            return node;
        }
        return null;
    }

    private List<GenericValue> parseArgsCallListChanger() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();
        List<GenericValue> list = new ArrayList<>();

        if (currentTokenType.equals("ID") ||
                currentTokenType.equals("INTEGER") ||
                currentTokenType.equals("OCTAL") ||
                currentTokenType.equals("HEX") ||
                currentTokenType.equals("FLOOAT") ||
                currentTokenType.equals("FLOAT") ||
                currentTokenType.equals("INT") ||
                currentTokenType.equals("BINARSUB") ||
                currentTokenType.equals("STRING")) {
            parseArgsCallList(list);
        }

        return list;
    }

    private void parseArgsCallList(List<GenericValue> list) throws CriticalParseException {
        GenericValue node = parseValueExpr();
        list.add(node);

        if (lexerList.getLookahead().getType().equals("COMMA")) {
            lexerList.match("COMMA");
            parseArgsCallList(list);
        }
    }


    private void parseStatementList(List<NodeStatement> statementList) throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("OPEN_CURL_BRACKET") ||
                currentTokenType.equals("WHILE_OPERATOR") ||
                currentTokenType.equals("IF_OPERATOR") ||
                currentTokenType.equals("INTEGER") ||
                currentTokenType.equals("HEX") ||
                currentTokenType.equals("OCTAL") ||
                currentTokenType.equals("FLOOAT") ||
                currentTokenType.equals("FLOAT") ||
                currentTokenType.equals("BINARSUB") ||
                currentTokenType.equals("STRING") ||
                currentTokenType.equals("ID") ||
                currentTokenType.equals("INT") ||
                currentTokenType.equals("CHAAR") ||
                currentTokenType.equals("RETURN") ||
                currentTokenType.equals("SEMICOLON") ||
                currentTokenType.equals("PRINTLN") ||
                currentTokenType.equals("SCANLN")) {
            statementList.add(parseStatement());
            parseStatementList(statementList);
        }
    }

    private List<NodeArgsInit> parseArgsInitListChanger() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("INT") ||
//                currentTokenType.equals("FLOAT") ||
                currentTokenType.equals("FLOOAT") ||
                currentTokenType.equals("CHAAR")) {
            List<NodeArgsInit> list = new ArrayList<>();
            parseArgsInitList(list);
            return list;
        }

        return null;
    }

    private void parseArgsInitList(List<NodeArgsInit> list) throws CriticalParseException {
        list.add(parseArgInit());

        if (lexerList.getLookahead().getType().equals("COMMA")) {
            lexerList.match("COMMA");
            parseArgsInitList(list);
        }
    }

    private NodeArgsInit parseArgInit() throws CriticalParseException {
        NodeArgsInit node = new NodeArgsInit();

        node.setDataType(lexerList.getLookahead());
        parseNativeDataType();
        boolean isArray = parseRValueFork();
        if (isArray) {
            node.setTypeInit(TypeInit.ARRAY);
        } else {
            node.setTypeInit(TypeInit.VAR);
        }

        node.setId(lexerList.getLookahead());
        lexerList.match("ID");

        return node;
    }

    private boolean parseRValueFork() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();

        if (currentTokenType.equals("OPEN_SQUARE_BRACKET")) {
            lexerList.match("OPEN_SQUARE_BRACKET");
            lexerList.match("CLOSE_SQUARE_BRACKET");
            return true;
        }

        return false;
    }

    private NodeLoop parseLoop() throws CriticalParseException {
        NodeLoop node = new NodeLoop();

        node.setWhileToken(lexerList.getLookahead());
        lexerList.match("WHILE_OPERATOR");

        lexerList.match("OPEN_BRACKET");

        node.setExpression(parseExpression());

        lexerList.match("CLOSE_BRACKET");
        lexerList.match("OPEN_CURL_BRACKET");

        List<NodeStatement> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for(NodeStatement statement : statementList) {
            node.addStatement(statement);
        }

        lexerList.match("CLOSE_CURL_BRACKET");

        return node;
    }

    private NodeConditional parseConditional() throws CriticalParseException {
        NodeConditional node = new NodeConditional();

        node.setIfToken(lexerList.getLookahead());
        lexerList.match("IF_OPERATOR");

        lexerList.match("OPEN_BRACKET");

        node.setExpression(parseExpression());

        lexerList.match("CLOSE_BRACKET");
        lexerList.match("OPEN_CURL_BRACKET");

        List<NodeStatement> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for (NodeStatement statement : statementList) {
            node.addStatement(statement);
        }

        lexerList.match("CLOSE_CURL_BRACKET");

        node.setElseNode(parseElse());

        return node;
    }

    private NodeElse parseElse() throws CriticalParseException {
        String currentTokenType = lexerList.getLookahead().getType();
        if (currentTokenType.equals("ELSE_OPERATOR")) {
            Token elseToken = lexerList.getLookahead();
            lexerList.match("ELSE_OPERATOR");

            currentTokenType = lexerList.getLookahead().getType();

            if (currentTokenType.equals("IF_OPERATOR")) {
                NodeIfElse nodeIfElse = new NodeIfElse();
                nodeIfElse.setElseToken(elseToken);
                nodeIfElse.setConditional(parseConditional());

                return nodeIfElse;
            } else if (currentTokenType.equals("OPEN_CURL_BRACKET")) {
                NodeJustElse nodeJustElse = new NodeJustElse();
                nodeJustElse.setElseToken(elseToken);
                lexerList.match("OPEN_CURL_BRACKET");

                List<NodeStatement> statementList = new ArrayList<>();
                parseStatementList(statementList);
                for (NodeStatement statement : statementList) {
                    nodeJustElse.addStatement(statement);
                }

                lexerList.match("CLOSE_CURL_BRACKET");

                return nodeJustElse;
            }
        }

        return null;
    }

    private Operator parseConditions() throws CriticalParseException {
        Operator node = new Operator(lexerList.getLookahead());

        lexerList.matchOneOf("LESS",
                "GREATER",
                "EqEQUAL",
                "NotEqual",
                "LessOrEq",
                "GreatOrEq",
                "EQUAL");

        return node;
    }

    private NodeReturn parseReturn() throws CriticalParseException {
        NodeReturn node = new NodeReturn();

        node.setReturnToken(lexerList.getLookahead());
        lexerList.match("RETURN");

        node.setExpression(parseExpression());

        return node;
    }

    private NodeMainMethod parseMainMethod() throws CriticalParseException {
        NodeMainMethod node = new NodeMainMethod();

        node.setPublicToken(lexerList.getLookahead());
        lexerList.match("PUBLIC");

        node.setStaticToken(lexerList.getLookahead());
        lexerList.match("STATIC");

        node.setVoidToken(lexerList.getLookahead());
        lexerList.match("VOID");

        node.setIdMainToken(lexerList.getLookahead());
        lexerList.matchTypeAndCheckValue("ID","main");

        lexerList.match("OPEN_BRACKET");

        node.setGettingDataTypeToken(lexerList.getLookahead());
        lexerList.matchTypeAndCheckValue("ID","String");

        lexerList.match("OPEN_SQUARE_BRACKET");
        lexerList.match("CLOSE_SQUARE_BRACKET");

        node.setIdArgsToken(lexerList.getLookahead());
        lexerList.match("ID");

        lexerList.match("CLOSE_BRACKET");
        lexerList.match("OPEN_CURL_BRACKET");

        List<NodeStatement> statementList = new ArrayList<>();
        parseStatementList(statementList);
        for(NodeStatement statement : statementList) {
            node.addStatement(statement);
        }

        lexerList.match("CLOSE_CURL_BRACKET");

        return node;
    }
}

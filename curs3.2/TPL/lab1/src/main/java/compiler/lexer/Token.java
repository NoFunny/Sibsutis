package compiler.lexer;

public class Token {
    private String type;
    private String value;
    private Location location;

    public  Token(String type, String value, Location location) {
        this.type = type;
        this.value = value;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public Location getLocation() {
        return location;
    }

    public String toString() {
        return location.toString() + " " + type + " '" + value + "'";
    }

    @Override
    public boolean equals(Object obj) {
        return (((String) obj).equals(this.type));
    }
}

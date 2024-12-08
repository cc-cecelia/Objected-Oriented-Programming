public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        this.next();
    }

    public void next() {
        skipBlank();
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if ("*".indexOf(c) != -1 && "*".indexOf(input.charAt(pos + 1)) != -1) {
            pos += 2;
            curToken = "**";
        } else if ("-+*()".indexOf(c) != -1 || Character.isLetter(c)) {
            pos++;
            curToken = String.valueOf(c);
        }
    }

    public void skipBlank() {
        if (pos < input.length()) {
            char current = input.charAt(pos);
            while (" \t".indexOf(current) != -1) {
                pos++;
                if (pos < input.length()) {
                    current = input.charAt(pos);
                } else {
                    return;
                }
            }
        }
    }

    public String getNumber() {
        StringBuilder sb = new StringBuilder();
        if (input.charAt(pos) == '0') {
            while (pos < input.length() && input.charAt(pos) == '0') {
                pos++;
            }
            if (pos == input.length() || !Character.isDigit(input.charAt(pos))) {
                return "0";
            }
        }
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos));
            pos++;
        }
        return sb.toString();
    }

    public String peek() {
        return this.curToken;
    }
}

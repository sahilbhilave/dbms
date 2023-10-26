import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

class ArgumentTable {
    Map<String, String> arguments = new HashMap<>();
}

class PassTwoMacroProcessor {
    static Map<String, Integer> macroNameTable = new HashMap<>();
    static Map<Integer, List<String>> macroDefinitionTable = new HashMap<>();
    static Map<String, ArgumentTable> argumentLabelTable = new HashMap<>();
    static int definitionCounter = 1;
    static int nameCounter = 1;

    private static void createTable(Map<String, Integer> table, String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line, " ", false);
                List<String> tokenList = new ArrayList<>();
                while (tokens.hasMoreTokens()) {
                    tokenList.add(tokens.nextToken());
                }
                table.put(tokenList.get(0), Integer.parseInt(tokenList.get(1)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDefinitionTable() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./MDTable.txt"));
            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line, " ", false);
                List<String> tokenList = new ArrayList<>();
                while (tokens.hasMoreTokens()) {
                    tokenList.add(tokens.nextToken());
                }
                macroDefinitionTable.put(i, tokenList);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createArgumentTable() {
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./ALAtable.txt"));
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                ArgumentTable argumentTable = new ArgumentTable();
                String current = line;
                while (!(line = reader.readLine()).equals("END")) {
                    StringTokenizer tokens = new StringTokenizer(line, " ", false);
                    List<String> tokenList = new ArrayList<>();
                    while (tokens.hasMoreTokens()) {
                        tokenList.add(tokens.nextToken());
                    }
                    argumentTable.arguments.put(tokenList.get(0), tokenList.get(1));
                }
                argumentLabelTable.put(current, argumentTable);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processMacro(List<String> tokenList, int index) {
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        createTable(macroNameTable, "./MNTtable.txt");
        createDefinitionTable();
        createArgumentTable();

        FileReader inputReader = new FileReader("./macroOutput.txt");
        FileWriter outputWriter = null;
        try {
            outputWriter = new FileWriter("./pass2Output.txt");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try (BufferedReader inputBufferedReader = new BufferedReader(inputReader)) {
            String line;
            while ((line = inputBufferedReader.readLine()) != null) {
                List<String> tokenList = new ArrayList<>();
                StringTokenizer tokens = new StringTokenizer(line, " ");
                while (tokens.hasMoreTokens()) {
                    tokenList.add(tokens.nextToken());
                }
                String currentToken = tokenList.get(0);

                if (macroNameTable.containsKey(currentToken)) {
                    String result = "";
                    int startIndex = macroNameTable.get(tokenList.get(0));
                    for (int i = startIndex + 1; !macroDefinitionTable.get(i).get(1).equals("MEND"); i++) {
                        List<String> definitionList = macroDefinitionTable.get(i);
                        result += definitionList.get(1) + " ";
                        for (int j = 2; j < definitionList.size(); j++) {
                            if (j == definitionList.size() - 1)
                                result += argumentLabelTable.get(currentToken).arguments.get(definitionList.get(j));
                            else
                                result += argumentLabelTable.get(currentToken).arguments.get(definitionList.get(j)) + ", ";
                        }
                        result += "\n";
                    }
                    outputWriter.write(result);
                } else {
                    outputWriter.write(line + "\n");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (outputWriter != null) {
                    outputWriter.flush();
                    outputWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

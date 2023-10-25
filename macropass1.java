import java.io.*;
import java.util.*;

class MacroDefinition {
    Map<String, String> arguments = new HashMap<>();
}

class Macro {
    private static Map<String, Integer> macroNameToCode = new HashMap<>();
    private static Map<Integer, List<String>> macroDefinitionTable = new HashMap<>();
    private static Map<String, MacroDefinition> argumentLabelTable = new HashMap<>();
    private static int macroDefinitionCode = 1;
    private static int macroNameCode = 1;

    private static void writeToMDTable() {
        try (FileWriter writer = new FileWriter("MDTable.txt")) {
            for (Integer code : macroDefinitionTable.keySet()) {
                String line = code + " " + String.join(" ", macroDefinitionTable.get(code)) + "\n";
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToMNTTable() {
        try (FileWriter writer = new FileWriter("MNTtable.txt")) {
            for (String macroName : macroNameToCode.keySet()) {
                String line = macroName + " " + macroNameToCode.get(macroName) + "\n";
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToALATable() {
        try (FileWriter writer = new FileWriter("ALAtable.txt")) {
            for (String macroName : argumentLabelTable.keySet()) {
                writer.write(macroName + "\n");
                MacroDefinition argument = argumentLabelTable.get(macroName);
                for (Map.Entry<String, String> argumentEntry : argument.arguments.entrySet()) {
                    String line = argumentEntry.getKey() + " " + argumentEntry.getValue() + "\n";
                    writer.write(line);
                }
                writer.write("END\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        FileReader inputFileReader = new FileReader("input.txt");
        try (FileWriter outputFileWriter = new FileWriter("macroOutput.txt")) {
            BufferedReader inputBufferedReader = new BufferedReader(inputFileReader);
            String line;
            int lineNumber = 0;

            while ((line = inputBufferedReader.readLine()) != null) {
                if (line.charAt(0) == 'S') {
                    break;
                }
                lineNumber++;
                List<String> tokens = new ArrayList<>(Arrays.asList(line.split(" ")));
                if (tokens.isEmpty()) {
                    continue;
                }
                if ("MACRO".equals(tokens.get(0))) {
                    line = inputBufferedReader.readLine();
                    List<String> macroDefinitionTokens = new ArrayList<>(Arrays.asList(line.split(" ")));
                    macroNameToCode.put(macroDefinitionTokens.get(0), macroDefinitionCode);
                    String currentMacroName = macroDefinitionTokens.get(0);
                    List<String> argumentList = new ArrayList<>(Arrays.asList(macroDefinitionTokens.get(1).split(",")));
                    MacroDefinition macroArgument = new MacroDefinition();
                    for (int i = 0; i < argumentList.size(); i++) {
                        String current = argumentList.get(i);
                        String currentKeyword;
                        String defaultValue = "";
                        if (current.contains("=")) {
                            int equalsPosition = current.indexOf('=');
                            if (equalsPosition == current.length() - 1) {
                                currentKeyword = current.substring(0, equalsPosition);
                                defaultValue = "@";
                            } else {
                                currentKeyword = current.substring(0, equalsPosition);
                                defaultValue = current.substring(equalsPosition + 1, current.length());
                            }
                        } else {
                            currentKeyword = current;
                            defaultValue = "#" + i;
                        }
                        macroArgument.arguments.put(currentKeyword, defaultValue);
                    }
                    argumentLabelTable.put(macroDefinitionTokens.get(0), macroArgument);
                    macroDefinitionTable.put(macroDefinitionCode++, macroDefinitionTokens);
                    while (!(line = inputBufferedReader.readLine()).equals("MEND")) {
                        List<String> tokens2 = new ArrayList<>(Arrays.asList(line.split(" ")));
                        String arguments = tokens2.get(1);
                        tokens2.remove(1);
                        List<String> argumentList2 = new ArrayList<>(Arrays.asList(arguments.split(",")));
                        tokens2.addAll(argumentList2);
                        macroDefinitionTable.put(macroDefinitionCode++, tokens2);
                    }
                    List<String> tempMend = new ArrayList<>();
                    tempMend.add("MEND");
                    macroDefinitionTable.put(macroDefinitionCode++, tempMend);
                } else {
                    continue;
                }
            }
            do {
                outputFileWriter.write(line + "\n");
                line = inputBufferedReader.readLine();
                List<String> tokens1 = new ArrayList<>(Arrays.asList(line.split(" ")));
                if (macroNameToCode.containsKey(tokens1.get(0))) {
                    String macroName = tokens1.get(0);
                    String actualArguments = tokens1.get(1);
                    List<String> argumentList2 = new ArrayList<>(Arrays.asList(actualArguments.split(",")));
                    for (int i = 0; i < argumentList2.size(); i++) {
                        String current = argumentList2.get(i);
                        if (current.contains("=")) {
                            int equalsPosition = current.indexOf('=');
                            String parameter = "&" + current.substring(0, equalsPosition);
                            String value = current.substring(equalsPosition + 1, current.length());
                            argumentLabelTable.get(macroName).arguments.put(parameter, value);
                        } else {
                            for (String key : argumentLabelTable.get(macroName).arguments.keySet()) {
                                if (argumentLabelTable.get(macroName).arguments.get(key).equals("#" + i)) {
                                    argumentLabelTable.get(macroName).arguments.put(key, current);
                                }
                            }
                        }
                    }
                }
            } while (!"END".equals(line));
            outputFileWriter.write("END\n");
            writeToMDTable();
            writeToMNTTable();
            writeToALATable();
            inputBufferedReader.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

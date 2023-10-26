import java.io.*;
import java.util.*;

class Ala {
    HashMap<String, String> Arguments = new HashMap<>();
}

class demo {
    static HashMap<String, Integer> MNT = new HashMap<>();
    static HashMap<Integer, ArrayList<String>> MDT = new HashMap<>();
    static HashMap<String, Ala> AlaTable = new HashMap<>();
    static int MDTC = 1;
    static int MNTC = 1;

    private static void PreapareMDT() {
        try (FileWriter writer = new FileWriter("MDTable.txt")) {
            for (Integer strKey : MDT.keySet()) {
                String temp = strKey + " " + String.join(" ", MDT.get(strKey)) + "\n";
                writer.write(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void PrepareMNT() {
        try (FileWriter writer = new FileWriter("MNTtable.txt")) {
            for (Map.Entry<String, Integer> entry : MNT.entrySet()) {
                String temp = entry.getKey() + " " + entry.getValue() + "\n";
                writer.write(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void AlaTable() throws IOException {
        try (FileWriter writer = new FileWriter("ALAtable.txt")) {
            for (Map.Entry<String, Ala> entry : AlaTable.entrySet()) {
                String temp = entry.getKey() + "\n";
                Ala argument = entry.getValue();
                for (Map.Entry<String, String> argumentEntry : argument.Arguments.entrySet()) {
                    temp += argumentEntry.getKey() + " " + argumentEntry.getValue() + "\n";
                }
                temp += "END\n";
                writer.write(temp);
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        FileReader fr_input = new FileReader("input.txt");
        try (FileWriter fr_output = new FileWriter("macroOutput.txt")) {
            BufferedReader br_input = new BufferedReader(fr_input);
            String s;
            int lineno = 0;

            while ((s = br_input.readLine()) != null) {
                if (s.charAt(0) == 'S') {
                    break;
                }
                lineno++;
                ArrayList<String> arrayList = new ArrayList<>();
                StringTokenizer tokens = new StringTokenizer(s, " ");
                while (tokens.hasMoreTokens()) {
                    arrayList.add(tokens.nextToken());
                }
                if (arrayList.isEmpty()) {
                    continue;
                }
                if ("MACRO".equals(arrayList.get(0))) {
                    s = br_input.readLine();
                    ArrayList<String> arrayList1 = new ArrayList<>();
                    StringTokenizer tokens1 = new StringTokenizer(s, " ");
                    while (tokens1.hasMoreTokens()) {
                        arrayList1.add(tokens1.nextToken());
                    }
                    MNT.put(arrayList1.get(0), MDTC);
                    String curr_macroname = arrayList1.get(0);
                    ArrayList<String> arrayList11 = new ArrayList<>();
                    StringTokenizer tokens11 = new StringTokenizer(arrayList1.get(1), ",");
                    while (tokens11.hasMoreTokens()) {
                        arrayList11.add(tokens11.nextToken());
                    }
                    Ala argument = new Ala();
                    for (int i = 0; i < arrayList11.size(); i++) {
                        String curr = arrayList11.get(i);
                        String curr_keyword;
                        String default_val = "";
                        if (curr.contains("=")) {
                            int positionEqu = curr.indexOf('=');
                            if (positionEqu == curr.length() - 1) {
                                curr_keyword = curr.substring(0, positionEqu);
                                default_val = "@";
                            } else {
                                curr_keyword = curr.substring(0, positionEqu);
                                String defaultValue = curr.substring(positionEqu + 1, curr.length());
                                default_val = defaultValue;
                            }
                        } else {
                            curr_keyword = curr;
                            default_val = "#" + i;
                        }
                        argument.Arguments.put(curr_keyword, default_val);
                    }
                    AlaTable.put(arrayList1.get(0), argument);
                    MDT.put(MDTC++, arrayList1);
                    while (!(s = br_input.readLine()).equals("MEND")) {
                        ArrayList<String> arrayList2 = new ArrayList<>();
                        ArrayList<String> arrayList22 = new ArrayList<>();
                        StringTokenizer tokens2 = new StringTokenizer(s, " ");
                        while (tokens2.hasMoreTokens()) {
                            arrayList2.add(tokens2.nextToken());
                        }
                        String argus = arrayList2.get(1);
                        arrayList2.remove(1);
                        StringTokenizer tokens3 = new StringTokenizer(argus, ",");
                        while (tokens3.hasMoreTokens()) {
                            arrayList22.add(tokens3.nextToken());
                        }
                        for (int i = 0; i < arrayList22.size(); i++) {
                            arrayList2.add(arrayList22.get(i));
                        }
                        MDT.put(MDTC++, arrayList2);
                    }
                    ArrayList<String> temp_mend = new ArrayList<>();
                    temp_mend.add("MEND");
                    MDT.put(MDTC++, temp_mend);
                } else {
                    continue;
                }
            }
            do {
                fr_output.write(s + "\n");
                s = br_input.readLine();
                ArrayList<String> arrayList1 = new ArrayList<>();
                StringTokenizer tokens1 = new StringTokenizer(s, " ");
                while (tokens1.hasMoreTokens()) {
                    arrayList1.add(tokens1.nextToken());
                }
                if (MNT.containsKey(arrayList1.get(0))) {
                    String macroName = arrayList1.get(0);
                    String actualArgs = arrayList1.get(1);
                    ArrayList<String> arrayList22 = new ArrayList<>();
                    StringTokenizer tokens3 = new StringTokenizer(actualArgs, ",");
                    while (tokens3.hasMoreTokens()) {
                        arrayList22.add(tokens3.nextToken());
                    }
                    for (int i = 0; i < arrayList22.size(); i++) {
                        String curr = arrayList22.get(i);
                        if (curr.contains("=")) {
                            int positionEqu = curr.indexOf('=');
                            String param = "&" + curr.substring(0, positionEqu);
                            String val = curr.substring(positionEqu + 1, curr.length());
                            AlaTable.get(macroName).Arguments.put(param, val);
                        } else {
                            for (String strKey : AlaTable.get(macroName).Arguments.keySet()) {
                                if (AlaTable.get(macroName).Arguments.get(strKey).equals("#" + i)) {
                                    AlaTable.get(macroName).Arguments.put(strKey, curr);
                                }
                            }
                        }
                    }
                }
            } while (!"END".equals(s));
            fr_output.write("END\n");
            PreapareMDT();
            PrepareMNT();
            AlaTable();
            br_input.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

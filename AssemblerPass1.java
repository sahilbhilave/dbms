import java.io.*;
import java.util.*;

class Tuple {
    String mnemonic, mclass, opcode;
    int length;

    Tuple(String s1, String s2, String s3, String s4) {
        mnemonic = s1;
        mclass = s2;
        opcode = s3;
        length = Integer.parseInt(s4.trim());
    }

    @Override
    public String toString() {
        return mnemonic + " " + mclass + " " + opcode + " " + length;
    }
}

class Prog {
    public static HashMap<String, Tuple> map = new HashMap<String, Tuple>();
	public static HashMap<String,Integer> registers = new HashMap<String, Integer>();
	
	public static ArrayList<String> literals = new ArrayList<String>();
	public static ArrayList<String> symbols = new ArrayList<String>();
	
    public Prog() {
        registers.put("AREG", 1);
		registers.put("BREG", 2);
		registers.put("CREG", 3);
		registers.put("DREG", 4);
    }

    public static void tokenizeMot() {
        try {
            FileInputStream input = new FileInputStream("mot.txt");
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\s+");

                if (parts.length == 4) {
                    String mnemonic = parts[0];
                    String mclass = parts[1];
                    String opcode = parts[2];
                    String length = parts[3];

                    map.put(mnemonic, new Tuple(mnemonic, mclass, opcode, length));
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void intermediateCode() {
        HashMap<String, String> out = new HashMap<String, String>();

        tokenizeMot();
        Prog program = new Prog();
        String[] inputArr = program.tokenizeInput();

        for (int i = 0; i < inputArr.length; i++) {
            if (map.containsKey(inputArr[i])) {
                Tuple tuple = map.get(inputArr[i]);
                String p1 = "(" + tuple.mclass + "," + tuple.mnemonic + ")";
                if (tuple.mclass.equals("AD")) {
                    if (tuple.mnemonic.equals("START")) {
                        // Handle START
                    }
                }

                out.put(p1, null);
            }
        }
    }

    public String[] tokenizeInput() {
        List<String> tokenList = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                for (String token : tokens) {
                    String trimmedToken = token.trim();
                    if (!trimmedToken.isEmpty()) {
                        tokenList.add(trimmedToken);
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Something went wrong! " + e);
        }

        return tokenList.toArray(new String[0]);
    }

    public static void writeHashMapToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            for (Map.Entry<String, Tuple> entry : map.entrySet()) {
                String key = entry.getKey();
                Tuple value = entry.getValue();
                writer.write(key + "," + value.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        intermediateCode();
        writeHashMapToFile();
    }
}

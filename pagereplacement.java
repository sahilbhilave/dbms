import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Scanner;

class FIFO {
    public int pageFaults(int pages[], int n, int capacity) {
        HashSet<Integer> s = new HashSet<>(capacity);
        Queue<Integer> indexes = new LinkedList<>();
        int page_faults = 0;
        
        for (int i = 0; i < n; i++) {
            if (s.size() < capacity) {
                if (!s.contains(pages[i])) {
                    s.add(pages[i]);
                    page_faults++;
                    indexes.add(pages[i]);
                }
            } else {
                if (!s.contains(pages[i])) {
                    int val = indexes.poll();
                    s.remove(val);
                    s.add(pages[i]);
                    indexes.add(pages[i]);
                    page_faults++;
                }
            }
        }
        return page_faults;
    }
}

class LRU {
    public void LRU_algo(int arr[], int capacity) {
        ArrayList<Integer> s = new ArrayList<>(capacity);
        int count = 0;
        int page_faults = 0;
        
        for (int i : arr) {
            if (!s.contains(i)) {
                if (s.size() == capacity) {
                    s.remove(0);
                    s.add(capacity - 1, i);
                } else
                    s.add(count, i);
                page_faults++;
                count++;
            } else {
                s.remove((Object) i);
                s.add(s.size(), i);
            }
        }
        System.out.println(page_faults);
    }
}

class Optimal {
    public void optimal_algo() {
        Scanner in = new Scanner(System.in);
        int frames = 0;
        int pointer = 0;
        int numFault = 0;
        int numhit = 0;
        int ref_len;
        boolean isFull = false;
        int buffer[];
        boolean hit[];
        int fault[];
        int reference[];
        int mem_layout[][];

        System.out.println("Please enter the number of frames: ");
        frames = Integer.parseInt(in.nextLine());
        System.out.println("Please enter the length of the reference string: ");
        ref_len = Integer.parseInt(in.nextLine());
        reference = new int[ref_len];
        mem_layout = new int[ref_len][frames];
        buffer = new int[frames];
        hit = new boolean[ref_len];
        fault = new int[ref_len];

        for (int j = 0; j < frames; j++) {
            buffer[j] = -1;
        }
        System.out.println("Please enter the reference string: ");
        for (int i = 0; i < ref_len; i++) {
            reference[i] = in.nextInt();
        }
        System.out.println();
        for (int i = 0; i < ref_len; i++) {
            int search = -1;
            for (int j = 0; j < frames; j++) {
                if (buffer[j] == reference[i]) {
                    search = j;
                    hit[i] = true;
                    fault[i] = numFault;
                    break;
                }
            }
            if (search == -1) {
                if (isFull) {
                    int index[] = new int[frames];
                    boolean index_flag[] = new boolean[frames];
                    for (int j = i + 1; j < ref_len; j++) {
                        for (int k = 0; k < frames; k++) {
                            if ((reference[j] == buffer[k]) && (index_flag[k] == false)) {
                                index[k] = j;
                                index_flag[k] = true;
                                break;
                            }
                        }
                    }
                    int max = index[0];
                    pointer = 0;
                    if (max == 0) {
                        max = 200;
                    }
                    for (int j = 0; j < frames; j++) {
                        if (index[j] == 0) {
                            index[j] = 200;
                        }
                        if (index[j] > max) {
                            max = index[j];
                            pointer = j;
                        }
                    }
                }
                buffer[pointer] = reference[i];
                numFault++;
                fault[i] = numFault;
                if (!isFull) {
                    pointer++;
                    if (pointer == frames) {
                        pointer = 0;
                        isFull = true;
                    }
                }
            }
            for (int j = 0; j < frames; j++) {
                mem_layout[i][j] = buffer[j];
            }
        }
        for (int i = 0; i < ref_len; i++) {
            System.out.print(reference[i] + ": Memory is: ");
            for (int j = 0; j < frames; j++) {
                if (mem_layout[i][j] == -1) {
                    System.out.printf("%3s ", "-1");
                } else {
                    System.out.printf("%3d ", mem_layout[i][j]);
                }
            }
            System.out.print(": ");
            if (hit[i]) {
                System.out.print("Hit");
                numhit++;
            } else {
                System.out.print("Page Fault");
                System.out.print(": (Number of Page Faults: " + fault[i] + ")");
            }
            System.out.println();
        }
        System.out.println("Total Number of Page Faults: " + numFault);
        System.out.println("Total Number of Hits: " + numhit);
    }
}

class Page {
    public static void main(String args[]) {
        int capacity;
        int n;
        FIFO fifo = new FIFO();
        LRU lru = new LRU();
        Optimal optimal = new Optimal();
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("Menu");
            System.out.println("1. FIFO");
            System.out.println("2. LRU");
            System.out.println("3. Optimal");
            System.out.println("4. exit");
            System.out.println("Select the algorithm you want to implement: ");
            int choice = in.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter the number of pages: ");
                    n = in.nextInt();
                    int pages[] = new int[n];
                    System.out.println("Enter the pages: ");
                    for (int i = 0; i < n; i++) {
                        pages[i] = in.nextInt();
                    }
                    System.out.println("Enter the capacity: ");
                    capacity = in.nextInt();
                    System.out.println("FIFO Output");
                    int faults = fifo.pageFaults(pages, n, capacity);
                    System.out.println("The number of page faults are: " + faults);
                    break;
                case 2:
                    System.out.println("Enter the number of pages: ");
                    n = in.nextInt();
                    int pages_lru[] = new int[n];
                    System.out.println("Enter the pages: ");
                    for (int i = 0; i < n; i++) {
                        pages_lru[i] = in.nextInt();
                    }
                    System.out.println("Enter the capacity: ");
                    capacity = in.nextInt();
                    System.out.println("LRU Output");
                    lru.LRU_algo(pages_lru, capacity);
                    break;
                case 3:
                    System.out.println("Optimal Output");
                    optimal.optimal_algo();
                    break;
                case 4:
                    System.out.println("Exiting the code...");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}

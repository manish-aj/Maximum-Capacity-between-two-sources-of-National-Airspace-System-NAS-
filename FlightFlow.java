import java.io.*;
import java.util.*;


public class FlightFlow {



    static Integer fordFulkerson(int source, int sink, int [][]graphMatrix) {

        if(source == -1 || sink==-1)
            return 0;

        int flowPath[] = new int[graphMatrix.length];
        int flow = 0;


        while(bfs(source,sink,flowPath,graphMatrix)) {
            int bottleneck = Integer.MAX_VALUE;
            int src = sink;
            while(src != source) {
                bottleneck = Math.min(bottleneck, graphMatrix[flowPath[src]][src]);
                src = flowPath[src];

            }
            flow += bottleneck;


            int destination = sink;
            while(destination != source) {
                int parent = flowPath[destination];
                graphMatrix[parent][destination] -= bottleneck;
                graphMatrix[destination][parent] += bottleneck;
                destination = flowPath[destination];
            }

        }

        return flow;
    }


    static String trim(String flightTime) {
        String time[] = flightTime.split(":");
        int l = Integer.parseInt(time[0]);
        int m = Integer.parseInt(time[1]);
        if(m > 30)
            l=l+1;
        String modifiedTime = String.valueOf(l);
        return modifiedTime;
    }

     static boolean  bfs(int source, int sink, int [] flowPath, int [][]graphMatrix) {

        boolean visited[]=new boolean[graphMatrix.length];
        Queue<Integer> queue=new LinkedList<>();
        queue.add(source);
        visited[source]=true;
        while(!queue.isEmpty()) {
            Integer u = queue.poll();

            for(int i = 0; i< graphMatrix.length; i++) {

                if(visited[i] == false && graphMatrix[u][i] >0) {
                    visited[i] = true;
                    queue.add(i);
                    flowPath[i] = u;

                }
            }


        }

        return visited[sink] == true;
    }

    public static void main(String args[]) {

        ArrayList<String> cityList;
        ArrayList<String> timeList;
        Map<String, HashMap<String, Integer>> graph;
        Map<String, Integer> combinations;
        int [][] graphMatrix;
        ArrayList<String> nodeList;


        HashSet<String> citiesSet = new HashSet<>();
        HashSet<String> timeSet = new HashSet<>();

        String csvFile = "src/flights.csv";
        String line = "";

        ArrayList<String> flightData = new ArrayList<>();

        try  {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {


                flightData.add(line);
                String[] lines = line.split(",");
                citiesSet.add(lines[0]);
                citiesSet.add(lines[1]);
                timeSet.add(trim(lines[2]));
                timeSet.add(trim(lines[3]));


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        cityList = new ArrayList<>(citiesSet);
        timeList = new ArrayList<>(timeSet);


        combinations = new HashMap<>();
        graph = new HashMap<>();
        for(String city : cityList) {
            for(String time : timeList) {
                String key = city + '-' + time;
                combinations.put(key,0);
            }
        }


        for( String key :combinations.keySet())
        {
            HashMap<String,Integer> value =  new HashMap<String,Integer>();
            value.putAll(combinations);

            graph.put(key,value);
        }


        for(String flight : flightData) {
            String schedule[] = flight.split(",");
            String origin = schedule[0] + "-" + trim(schedule[2]);
            String destination = schedule[1] + "-" + trim(schedule[3]);

            graph.get(origin).put(destination, graph.get(origin).getOrDefault(destination,0) + Integer.parseInt(schedule[4]));

        }

        nodeList = new ArrayList<>(graph.keySet());
        graphMatrix = new int[nodeList.size()][nodeList.size()];
        int i=0,j=0;
        for(String x : nodeList) {
            j=0;
            for(String y : nodeList)
            {
                graphMatrix[i][j]= graph.get(x).get(y);
                j++;
            }
            i++;
        }

        Integer maximumFlow=0;

        for(String depart : timeList)
            for(String arrive : timeList) {

                if(Integer.parseInt(arrive) > Integer.parseInt(depart)) {
                    Integer flow = fordFulkerson(nodeList.indexOf("LAX-" + depart), nodeList.indexOf("JFK-" + arrive), graphMatrix);
                    maximumFlow += flow;


                }
            }
        System.out.println("Maximum Flow = " + maximumFlow);
    }


}

package sample;

import com.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.*;
import java.util.*;

public class Controller {
    @FXML
    private ComboBox<String> start;
    @FXML
    private ComboBox<String> end;
    @FXML
    private Button makeGo;
    @FXML
    private Label result;


    @FXML
    public void initialize() {
        Node[] nodes =initNode();
        for (Node n: nodes
        ) {
            start.getItems().add(n.value);
            end.getItems().add(n.value);
        }
        makeGo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node[] nodes =initNode();
                Node startNode =null , endNode = null;
                String s ="";
                result.setText("");
                if(start.getValue()==null && end.getValue() ==null){
                    return;
                }

                for (Node n: nodes
                ) {
                   if(start.getValue().equals(n.value)){
                       startNode = n;
                   }
                   if(end.getValue().equals(n.value)){
                       endNode = n;
                   }
                }

                s =Dijkstra(startNode,endNode);
                result.setText(s);
            }
        });

    }
    private String[][] readCsv (){
        List<String[]> list=null;
        String[][] dataArr =null;
        try {
            CSVReader csvReader = new CSVReader(new FileReader(new File("src/sample/test.csv")));
          list = csvReader.readAll();
        }catch (Exception e){e.printStackTrace();}
    if(list!=null) {
        dataArr = new String[list.size()][];
        dataArr = list.toArray(dataArr);
      }
        return dataArr;
    }

    private Node[] initNode(){
        String[][] dataArr =readCsv();
        Node[] nodes = new Node[dataArr.length-1];
        for (int i = 0; i < dataArr.length -1; i++) {
            nodes[i] = new Node(dataArr[0][i+1]);
        }

        for (int i = 0; i < dataArr.length-1; i++) {
            int lenght=0;
            int count = 0;
            for (int j = 0; j < dataArr.length-1; j++) {
                if(Integer.parseInt(dataArr[i+1][j+1]) > 0 ){
                  lenght++;
                }
            }
            nodes[i].adjacencies = new Edge[lenght];
            for (int k = 0; k <   dataArr.length-1; k++) {
                if(Integer.parseInt(dataArr[i+1][k+1]) > 0 ){
                    nodes[i].adjacencies[count] = new Edge(nodes[k],Integer.parseInt(dataArr[i+1][k+1]));
                    count++;
                }

            }
        }

        return nodes;
    }
    private  String Dijkstra(Node source, Node end) {

        String s ="";
        computePaths(source,end);
        List<Node> path = getShortestPathTo(end);
        for (Node n:path
        ) {
            s += (n.value+" "+"("+(int)n.shortestDistance+")");
            if (n != path.get(path.size() - 1)) {
                s += "---->";
            }

        }
        return s;

    }
    public  void computePaths(Node source,Node end){
        String str = "";
        int i =0;
        source.shortestDistance=0;
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        queue.add(source);
        for(int ii = 30;ii>0;ii--){
            System.out.println();
        }
        while(!queue.isEmpty()){
            Node u = queue.poll();
            for(Edge e: u.adjacencies){
                Node v = e.target;
                double weight = e.weight;
                double distanceFromU = u.shortestDistance+weight;

                if(distanceFromU<v.shortestDistance) {
                    queue.remove(v);
                    v.shortestDistance = distanceFromU;
                    v.parent = u;
                    queue.add(v);
                }
                System.out.println("times : "+i+" check all short path Node by Node : "+u+" to "+v+" is : "+getShortestPathTo(v).toString());
                i++;
            }
        }
        List<Node> path = getShortestPathTo(end);
        path=getShortestPathTo(end);
        System.out.print("So that the shortest path is : ");
        for (Node a : path){
            str += (a.value+" "+"("+(int)a.shortestDistance+")");
            if (a != path.get(path.size() - 1)) {
                str += "---->";
            }
        }
        System.out.print(str+"\n\n");






    }

    public  List<Node> getShortestPathTo(Node target){

        //trace path from target to source
        List<Node> path = new ArrayList<Node>();
        for(Node node = target; node!=null; node = node.parent){
            path.add(node);
        }


        Collections.reverse(path);

        return path;
    }

}
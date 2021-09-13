
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.PriorityQueue;



class Edge
{
	int source, dest; 
        float  weight;

	public Edge(int source, int dest, float weight) {
		this.source = source;
		this.dest = dest;
		this.weight = weight;
	}
}
public class BucketSort {
    public void bucketSort(List<Edge> edges, int n) {
    if (n <= 0)
      return;
    @SuppressWarnings("unchecked")
	
	//create n empty buckets
    ArrayList<Float>[] bucket = new ArrayList[n];
    
    
    PriorityQueue<Float> pq=new PriorityQueue<Float>();
	
	//create list in every bucket
    for (int i = 0; i < n; i++)
      bucket[i] = new ArrayList<Float>();
    
        int [] parent = new int[n];
        
        makeSet(parent,n);
        
        
	//put array elements in diffrent buckets
    for (int i = 0; i < n; i++) {
      float w = edges.get(i).weight;
      int bucketIndex = (int) w * n;
      bucket[bucketIndex].add(w);
    }
    
	//sort individual buckets
    for (int i = 0; i < n; i++) {
      Collections.sort((bucket[i]));
    }
	//concatenate all buckets into edges
                int index = 0;
                for (int i = 0; i < n; i++) {
                    for (int j = 0, size = bucket[i].size(); j < size; j++) {
                                edges.get(index).weight = bucket[i].get(j);
                                pq.add(edges.get(index++).weight);
                                
    }
    }
                int count=0;
                for(int i=0; i<n-1;i++){
                    edges.get(count).weight= pq.remove();
                    int x_set=find(parent, edges.get(count).source);
                    int y_set=find(parent, edges.get(count).dest);
                    
                    if(x_set==y_set){
                        continue;
                    }else{
                        pq.add(edges.get(i).weight);
                        union(parent,x_set,y_set);
                        count++;
                    }
                }
                
    }
    
    
    
     public void makeSet(int [] parent, int n){
            //Make set- creating a new element with a parent pointer to itself.
            for (int i = 0; i <n ; i++) {
                parent[i] = i;
            }
        }
     
    //check if is already in the tree
    public int find(int [] parent, int vertex){
            if(parent[vertex]!= vertex)
                return find(parent,parent[vertex]);
            return vertex;
    }
    
    //combine into the fuckin tree
    public void union(int [] parent, int x, int y){
        int x_set_parent=find(parent,x);
        int y_set_parent=find(parent,y);
        
        parent[y_set_parent]=x_set_parent;
    }

    
    public static void main(String[] args) {
        BucketSort b = new BucketSort();
    List<Edge> edges = Arrays.asList(
				// (x, y, w) -> edge from x to y having weight w
				new Edge( 0, 1, (float) 0.5),
                                new Edge( 2, 0, (float) 0.65 ),
				new Edge( 1, 2, (float) 0.55 ), 
                                new Edge( 2, 3,(float) 0.78 ),
				new Edge( 3, 4, (float)0.67 ));
    
    b.bucketSort(edges, 5);

    for(int i=0; i<5;i++){
        System.out.println(edges.get(i).weight);
    }
  }
    }
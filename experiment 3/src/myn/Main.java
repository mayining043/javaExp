package myn;

import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[]args){
		FileInputStream in1 = null,in2 = null;
		try {
			in1=new FileInputStream("./KMeans_Set1.txt");
			in2=new FileInputStream("./KMeans_Set2.txt");
		} catch (FileNotFoundException e1) {
			System.out.print("文件不存在！");
			System.exit(1);
		}
		System.out.println("请输入正整数K");
		Scanner input = new Scanner(System.in);
		int k = input.nextInt();
		System.out.println("请输入最大迭代数N");
		int n = input.nextInt();
		int i;
		System.out.println("\n第一组数据：");
		K_Means temp1 = new K_Means(k,in1,System.out);
		try {
			i=0;
			double []sse1=new double[2];
			while(i<n){
				i++;
				System.out.println("第"+i+"次迭代：");
				temp1.nextGeneration();
				temp1.print();
				sse1[i%2==0?0:1]=temp1.getSSE();
				if(i>1)
					if(Math.abs(sse1[1]-sse1[0])<0.0000000001)
						break;
			}
			System.out.println("运行结束，共迭代"+i+"次");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("\n第二组数据：");
		K_Means temp2 = new K_Means(k,in2,System.out);
		try {
			i=0;
			double []sse2=new double[2];
			while(i<n){
				i++;
				System.out.println("第"+i+"次迭代：");
				temp2.nextGeneration();
				temp2.print();
				sse2[i%2==0?0:1]=temp2.getSSE();
				if(i>1)
					if(Math.abs(sse2[1]-sse2[0])<0.00000000001)
						break;
			}
			System.out.println("运行结束，共迭代"+i+"次");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}
}

class K_Means{
	//聚类大小
	public int _k;
	//数据个数
	protected int n;
	private dataVector[]dat;
	private Point[]div;
	//输入输出流
	private InputStream _filein;
	private BufferedInputStream _bufin;
	private Scanner jin;
	private OutputStream _fileout;
	private BufferedOutputStream _bufout;
	private DataOutputStream jout;
	private boolean ok=false;
	//构造函数
	public K_Means(int k,InputStream filein,OutputStream fileout){
		_k=k;
		_filein=filein;
		_fileout=fileout;
		ok=init()?true:false;
	}
	/**
	 * @param 初始化输入输出流,并读入数据初始化k个簇
	 * @return is right
	 * @exception IOException
	 */
	public boolean init(){
		//初始化输入输出流
		try{
			_bufin=new BufferedInputStream(_filein);
			jin=new Scanner(_bufin);
			_bufout=new BufferedOutputStream(_fileout);
			jout=new DataOutputStream(_bufout);
		}
		catch(Exception e){
			System.out.println("输入输出流初始化失败，错误为：");
			System.out.println(e.toString());
			return false;
		}
		//读入数据
		n=0;
		Stack<Point> p=new Stack<Point>();
		try{ 
			while(jin.hasNextDouble()){
				 double x,y;
				 x=jin.nextDouble();
				 y=jin.nextDouble();
				 Point a=new Point(x,y);
				 n++;
				 p.push(a);
			}
		}
		catch(Exception e){
			System.out.println("数据读入失败，错误为：");
			System.out.println(e.toString());
			return false;
		}
		//初始化k个簇
		Stack<Point> p2=new Stack<Point>();
		dat=new dataVector[_k]; 
		for(int i=0;i<_k;i++)
			dat[i]=new dataVector();
		div=new Point[_k];
		for(int i=0;i<_k;i++)
			div[i]=new Point();
		Point a=null;
		for(int i=0;i<_k;i++){
			for(int ii=0;ii<n/(_k+3);ii++){
				a=p.pop();
				p2.push(a);
			}
			div[i]=a;
		}
		//初始化簇内元素
		while(!p2.isEmpty()){
			Point cur=p2.pop();
			int pos=this.findMin(cur);
			try{
				dat[pos].dat.add(cur);
				//System.out.print(pos);
			}
			catch(Exception e){
				System.out.println("数据插入失败");		
			}
		}
		while(!p.isEmpty()){
			Point cur=p.pop();
			int pos=this.findMin(cur);
			try{
				dat[pos].dat.add(cur);
				//System.out.print(pos);
			}
			catch(Exception e){
				System.out.println("数据插入失败");		
			}
		}
		System.out.println("数据共有："+n);
		System.out.println("初始化质点为：");
			try {
				for(int i=0;i<_k;i++)
				jout.writeBytes("["+div[i].getX()+","+div[i].getY()+"] \n");
				jout.writeBytes("\n");
				jout.flush();
			} catch (IOException e) {}
		return true;
	}
	/**
	 * 下一代
	 * @throws Exception
	 */
	public void nextGeneration()throws Exception{
		while(this.checkEmpty());
		//print();
		this.reCalc();
		//System.out.println("");
		this.resetVal();	
		//System.out.println("");
		//print();
		//System.out.println("");
		
	}
	/**
	 * 重新分配下一代簇
	 * @throws Exception
	 */
	protected void resetVal()throws Exception{
		if(!ok)
			throw new Exception("未初始化");
		//缓存数据
		Stack<Point> p=new Stack<Point>();
		Point a=new Point();
		p.clear();
		for(int i=0;i<_k;i++)
			for(int j=0;j<dat[i].dat.size();j++){
				a=dat[i].dat.elementAt(j);
				p.push(a);
			}
		//System.out.println("数据共有："+p.size());
		//更新数据
		dat=new dataVector[_k];
		for(int i=0;i<_k;i++){
			dat[i]=new dataVector();
			dat[i].dat.clear();
		}
		while(!p.isEmpty()){
			Point cur=p.pop();
			int pos=this.findMin(cur);
			try{
				dat[pos].dat.add(cur);
				//System.out.print(pos);
			}
			catch(Exception e){
				System.out.println("更新数据插入失败");		
			}
		}
	}
	/**
	 * 重新计算簇中心
	 * @throws Exception
	 */
	protected void reCalc()throws Exception{
		if(!ok)
			throw new Exception("未初始化");
		for(int i=0;i<_k;i++){
			div[i]=new Point(Point.avgPoint(dat[i].dat));
		}
	}
	/**
	 * 寻找最适宜的簇位置
	 * @param cur
	 * @return
	 */
	protected int findMin(Point cur){
		double min=Double.MAX_VALUE;
		int pos=0;
		for(int i=0;i<_k;i++)
			if(min>cur.distance(div[i])){
				pos=i;
				min=cur.distance(div[i]);
			}
		return pos;
	}
	/**
	 * 打印簇中点
	 * @throws Exception
	 */
	public void print()throws Exception{
		if(!ok)
			throw new Exception("未初始化");
		for(int i=0;i<_k;i++)
			jout.writeBytes("["+div[i].getX()+","+div[i].getY()+"] \n");
		jout.writeBytes("sum="+this.getSSE()+"");
		jout.writeBytes("\n");
		jout.flush();
	}
	/**
	 * 检查处理空簇
	 * @return 是否完成
	 * @throws Exception
	 */
	protected boolean checkEmpty()throws Exception{
		boolean con=false;
		if(!ok)
			throw new Exception("未初始化");
		for(int i=0;i<_k;i++){
			if(dat[i].dat.isEmpty()){
				System.out.println("出现空簇");
				double[] SSE=new double[_k];
				for(int ii=0;ii<_k;ii++){
					if(dat[ii].dat.isEmpty()){
						SSE[ii]=Double.MIN_VALUE;
						continue;
					}
					for(int j=0;j<dat[ii].dat.size();j++){
						Point cur=dat[ii].dat.elementAt(j);
						SSE[ii]+=div[ii].distance(cur);
					}
					
				}
				int pos=0;
				double max=Double.MIN_VALUE;
				for(int ii=0;ii<_k;ii++){
					if(SSE[ii]>max){
						pos=ii;
						max=SSE[ii];
					}
					//System.out.println(SSE[ii]);
				}
				int po=(int)Math.random()*dat[pos].dat.size();
				Point temp=dat[pos].dat.elementAt(po);
				div[i]=temp;
				con=true;
			}
			
		}
		if(con){
			this.resetVal();
			return true;
		}
		return false;
	}
	/**
	 * 计算均值和
	 * @return SSE
	 * @throws Exception
	 */
	public double getSSE()throws Exception{
		if(!ok)
			throw new Exception("未初始化");
		double SSE=0;
		for(int i=0;i<_k;i++){
			for(int j=0;j<dat[i].dat.size();j++){
				Point cur=(Point) dat[i].dat.get(j);
				SSE+=div[i].distance(cur);
			}
		}
		return SSE;
	}
}
class Point{
	private double _x;
	private double _y;
	public Point(){}
	public Point(Point p){
		_x=p.getX();
		_y=p.getY();
	}
	public Point(double x,double y){
		_x=x;
		_y=y;
	}
	/**
	 * set X,Y
	 * @param x
	 * @param y
	 */
	public void setXY(double x,double y){
		_x=x;
		_y=y;
	}
	/**
	 * 获得点的坐标值
	 * @return
	 */
	public double getX(){
		return _x;
	}
	public double getY(){
		return _y;
	}
	/**
	 * 计算两个点的距离
	 * @param p2
	 * @return distance of two Point
	 */
	public double distance(Point p2){
		double X1=this.getX();
		double X2=p2.getX();
		double Y1=this.getY();
		double Y2=p2.getY();
		return Math.sqrt(Math.pow(X1-X2,2)+Math.pow(Y1-Y2,2));
	}
	/**
	 * 返回Point数组中各店的平均点
	 * @param arr
	 * @return new Point
	 */
	public static Point avgPoint(Vector<Point> arr){
		double sum_x=0,sum_y=0;
		int len=arr.size();
		for(int i=0;i<len;i++){
			Point temp=arr.elementAt(i);
			sum_x+=temp.getX();
			sum_y+=temp.getY();
		}
		Point my=new Point(sum_x/len,sum_y/len);
		return my;
	}
	public void print(){
		System.out.println("["+getX()+","+getY()+"]  ");
	}
}
class dataVector{
	public dataVector(){
		dat=new Vector<Point>();
	}
	public Vector<Point>dat;
}
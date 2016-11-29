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
			System.out.print("�ļ������ڣ�");
			System.exit(1);
		}
		System.out.println("������������K");
		Scanner input = new Scanner(System.in);
		int k = input.nextInt();
		System.out.println("��������������N");
		int n = input.nextInt();
		int i;
		System.out.println("\n��һ�����ݣ�");
		K_Means temp1 = new K_Means(k,in1,System.out);
		try {
			i=0;
			double []sse1=new double[2];
			while(i<n){
				i++;
				System.out.println("��"+i+"�ε�����");
				temp1.nextGeneration();
				temp1.print();
				sse1[i%2==0?0:1]=temp1.getSSE();
				if(i>1)
					if(Math.abs(sse1[1]-sse1[0])<0.0000000001)
						break;
			}
			System.out.println("���н�����������"+i+"��");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		System.out.println();
		System.out.println("\n�ڶ������ݣ�");
		K_Means temp2 = new K_Means(k,in2,System.out);
		try {
			i=0;
			double []sse2=new double[2];
			while(i<n){
				i++;
				System.out.println("��"+i+"�ε�����");
				temp2.nextGeneration();
				temp2.print();
				sse2[i%2==0?0:1]=temp2.getSSE();
				if(i>1)
					if(Math.abs(sse2[1]-sse2[0])<0.00000000001)
						break;
			}
			System.out.println("���н�����������"+i+"��");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

	}
}

class K_Means{
	//�����С
	public int _k;
	//���ݸ���
	protected int n;
	private dataVector[]dat;
	private Point[]div;
	//���������
	private InputStream _filein;
	private BufferedInputStream _bufin;
	private Scanner jin;
	private OutputStream _fileout;
	private BufferedOutputStream _bufout;
	private DataOutputStream jout;
	private boolean ok=false;
	//���캯��
	public K_Means(int k,InputStream filein,OutputStream fileout){
		_k=k;
		_filein=filein;
		_fileout=fileout;
		ok=init()?true:false;
	}
	/**
	 * @param ��ʼ�����������,���������ݳ�ʼ��k����
	 * @return is right
	 * @exception IOException
	 */
	public boolean init(){
		//��ʼ�����������
		try{
			_bufin=new BufferedInputStream(_filein);
			jin=new Scanner(_bufin);
			_bufout=new BufferedOutputStream(_fileout);
			jout=new DataOutputStream(_bufout);
		}
		catch(Exception e){
			System.out.println("�����������ʼ��ʧ�ܣ�����Ϊ��");
			System.out.println(e.toString());
			return false;
		}
		//��������
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
			System.out.println("���ݶ���ʧ�ܣ�����Ϊ��");
			System.out.println(e.toString());
			return false;
		}
		//��ʼ��k����
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
		//��ʼ������Ԫ��
		while(!p2.isEmpty()){
			Point cur=p2.pop();
			int pos=this.findMin(cur);
			try{
				dat[pos].dat.add(cur);
				//System.out.print(pos);
			}
			catch(Exception e){
				System.out.println("���ݲ���ʧ��");		
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
				System.out.println("���ݲ���ʧ��");		
			}
		}
		System.out.println("���ݹ��У�"+n);
		System.out.println("��ʼ���ʵ�Ϊ��");
			try {
				for(int i=0;i<_k;i++)
				jout.writeBytes("["+div[i].getX()+","+div[i].getY()+"] \n");
				jout.writeBytes("\n");
				jout.flush();
			} catch (IOException e) {}
		return true;
	}
	/**
	 * ��һ��
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
	 * ���·�����һ����
	 * @throws Exception
	 */
	protected void resetVal()throws Exception{
		if(!ok)
			throw new Exception("δ��ʼ��");
		//��������
		Stack<Point> p=new Stack<Point>();
		Point a=new Point();
		p.clear();
		for(int i=0;i<_k;i++)
			for(int j=0;j<dat[i].dat.size();j++){
				a=dat[i].dat.elementAt(j);
				p.push(a);
			}
		//System.out.println("���ݹ��У�"+p.size());
		//��������
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
				System.out.println("�������ݲ���ʧ��");		
			}
		}
	}
	/**
	 * ���¼��������
	 * @throws Exception
	 */
	protected void reCalc()throws Exception{
		if(!ok)
			throw new Exception("δ��ʼ��");
		for(int i=0;i<_k;i++){
			div[i]=new Point(Point.avgPoint(dat[i].dat));
		}
	}
	/**
	 * Ѱ�������˵Ĵ�λ��
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
	 * ��ӡ���е�
	 * @throws Exception
	 */
	public void print()throws Exception{
		if(!ok)
			throw new Exception("δ��ʼ��");
		for(int i=0;i<_k;i++)
			jout.writeBytes("["+div[i].getX()+","+div[i].getY()+"] \n");
		jout.writeBytes("sum="+this.getSSE()+"");
		jout.writeBytes("\n");
		jout.flush();
	}
	/**
	 * ��鴦��մ�
	 * @return �Ƿ����
	 * @throws Exception
	 */
	protected boolean checkEmpty()throws Exception{
		boolean con=false;
		if(!ok)
			throw new Exception("δ��ʼ��");
		for(int i=0;i<_k;i++){
			if(dat[i].dat.isEmpty()){
				System.out.println("���ֿմ�");
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
	 * �����ֵ��
	 * @return SSE
	 * @throws Exception
	 */
	public double getSSE()throws Exception{
		if(!ok)
			throw new Exception("δ��ʼ��");
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
	 * ��õ������ֵ
	 * @return
	 */
	public double getX(){
		return _x;
	}
	public double getY(){
		return _y;
	}
	/**
	 * ����������ľ���
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
	 * ����Point�����и����ƽ����
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
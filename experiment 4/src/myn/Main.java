package myn;
import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args){
		FileInputStream in1 = null,in2 = null;
		try {
			in1=new FileInputStream("./LR_ex0.txt");
			in2=new FileInputStream("./LR_ex1.txt");
		} catch (FileNotFoundException e1) {
			System.out.print("�ļ������ڣ�");
			System.exit(1);
		}
		System.out.println("��һ�����ݣ�");
		LinearRegression temp1=new LinearRegression(in1);
		temp1.run();

		System.out.println("\n�ڶ������ݣ�");
		LinearRegression temp2 = new LinearRegression(in2);
		temp2.run();
	}
}

class LinearRegression {
	//���������
	private InputStream _filein;
	private BufferedInputStream _bufin;
	private Scanner jin;
	//���ݱ���
	private int n = 0;
	private Vector<Point>dat;
	private double averageX = 0;
	private double averageY = 0;
	private double A = 0;
	private double B = 0;
	//���캯��
	public LinearRegression(InputStream filein){
			_filein=filein;
			initData();
	}
	public void initData(){
		//��ʼ�����������
		try{
			_bufin=new BufferedInputStream(_filein);
			jin=new Scanner(_bufin);
		}
		catch(Exception e){
			System.out.println("�����������ʼ��ʧ�ܣ�����Ϊ��");
			System.out.println(e.toString());
			return;
		}
		//��������
		n=0;
		dat=new Vector<Point>();
		try{ 
			while(jin.hasNextDouble()){
				 double x,y;
				 jin.nextDouble();
				 x=jin.nextDouble();
				 y=jin.nextDouble();
				 Point a=new Point(x,y);
				 n++;
				 dat.add(a);
			}
		}
		catch(Exception e){
			System.out.println("���ݶ���ʧ�ܣ�����Ϊ��");
			System.out.println(e.toString());
			return;
		}
		System.out.println("��ȡ����"+n+"��");
	}
	
	public void calc(){
		double totalX =0;
		double totalY =0;
		double totalXY =0;
		double totalXX =0;
		for(int i=0;i<n;i++){
			Point temp=dat.elementAt(i);
			totalX+=temp.getX();
			totalY+=temp.getY();
			totalXY+=temp.getX()*temp.getY();
			totalXX+=temp.getX()*temp.getX();
		}
		this.averageX=totalX/n;
		this.averageY=totalY/n;
		this.A =(totalXY-n*averageX*averageY)/
				(totalXX-n*averageX*averageX);
		this.B =averageY-averageX*A;
		System.out.println("���������");
	}
	public void show(){
		System.out.println("f(x) = A * X + B �� : ");
		System.out.println("A = "+A);
		System.out.println("B = "+B);
	}
	public void run() {
		calc();
		show();
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
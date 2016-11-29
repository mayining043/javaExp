package myn;

import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[]args){
		try{
			FileInputStream in=new FileInputStream(new File("./score.csv"));
			Sc temp=new Sc(in,new File("./data_out.txt"));
			temp.run();
		}
		catch(Exception e){
			System.out.print("文件操作失败");
			System.exit(1);
		}
		
	}
}

class Sc{
	//输入输出流
	private InputStream _filein;
	private BufferedInputStream _bufin;
	private Scanner jin;
	private FileOutputStream _fileout;
	private OutputStreamWriter jout;
	private Vector<Student>_class;
	//数据答案
	private int min_val=Integer.MAX_VALUE;
	private int min_index=-1;
	private int max_val=Integer.MIN_VALUE;
	private int max_index=-1;
	private int sum=0;
	private int SA=0;
	private int SB=0;
	private int SC=0;
	private int SD=0;
	/**
	 * 构造函数
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public Sc(InputStream in,File out)throws IOException{
		//构造输入输出流
		_filein=in;
		_bufin=new BufferedInputStream(_filein);
		jin=new Scanner(_bufin);
		//正则表达式
		jin.useDelimiter("[\\s\\;]");
		_fileout=new FileOutputStream(out);
		jout=new OutputStreamWriter(_fileout,"UTF-8");
		//读入数据
		_class=new Vector<Student>();
		while(jin.hasNext()){
			Student p=new Student(jin.nextInt(),jin.nextInt());
			_class.addElement(p);
		}
		jin.close();
		System.out.println("成功读入"+_class.size()+"个数据");
	}
	/**
	 * 运行计算
	 * @throws IOException 
	 */
	public void run() throws IOException{
		int len=_class.size();
		for(int i=0;i<len;i++){
			sum+=_class.elementAt(i).s;
			if(_class.elementAt(i).s>max_val){
				max_val=_class.elementAt(i).s;
				max_index=i;
			}
			if(_class.elementAt(i).s<min_val){
				min_val=_class.elementAt(i).s;
				min_index=i;
			}	
			if (_class.elementAt(i).s>=60
					&&_class.elementAt(i).s<=69)
				SD++;
			else if (_class.elementAt(i).s>=70
					&&_class.elementAt(i).s<=79)
				SC++;
			else if (_class.elementAt(i).s>=80
					&&_class.elementAt(i).s<=89)
				SB++;
			else if (_class.elementAt(i).s>=90
					&&_class.elementAt(i).s<=100)
				SA++;
		}
		jout.write("最高成绩 : "+max_val+"分，其学号为："+max_index+"\r\n");
		jout.write("最低成绩 : "+min_val+"分，其学号为："+min_index+"\r\n");
		jout.write("平均分："+((double)sum)/((double)len)+"\r\n");
		jout.write("成绩统计 :  \r\n");
		jout.write("60~69: "+SD+"个\r\n");
		jout.write("70~79: "+SC+"个\r\n");
		jout.write("80~89: "+SB+"个\r\n");
		jout.write("90~100: "+SA+"个\r\n");
		jout.close();
		System.out.println("处理完成");
		System.out.println("请查看"+"./data_out.txt"+"\n(UTF-8)");
	}
}
class Student{
	public int id;
	public int s;
	public Student(int id,int s){
		this.id=id;
		this.s=s;
	}
}
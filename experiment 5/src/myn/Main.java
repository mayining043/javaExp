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
			System.out.print("�ļ�����ʧ��");
			System.exit(1);
		}
		
	}
}

class Sc{
	//���������
	private InputStream _filein;
	private BufferedInputStream _bufin;
	private Scanner jin;
	private FileOutputStream _fileout;
	private OutputStreamWriter jout;
	private Vector<Student>_class;
	//���ݴ�
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
	 * ���캯��
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	public Sc(InputStream in,File out)throws IOException{
		//�������������
		_filein=in;
		_bufin=new BufferedInputStream(_filein);
		jin=new Scanner(_bufin);
		//������ʽ
		jin.useDelimiter("[\\s\\;]");
		_fileout=new FileOutputStream(out);
		jout=new OutputStreamWriter(_fileout,"UTF-8");
		//��������
		_class=new Vector<Student>();
		while(jin.hasNext()){
			Student p=new Student(jin.nextInt(),jin.nextInt());
			_class.addElement(p);
		}
		jin.close();
		System.out.println("�ɹ�����"+_class.size()+"������");
	}
	/**
	 * ���м���
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
		jout.write("��߳ɼ� : "+max_val+"�֣���ѧ��Ϊ��"+max_index+"\r\n");
		jout.write("��ͳɼ� : "+min_val+"�֣���ѧ��Ϊ��"+min_index+"\r\n");
		jout.write("ƽ���֣�"+((double)sum)/((double)len)+"\r\n");
		jout.write("�ɼ�ͳ�� :  \r\n");
		jout.write("60~69: "+SD+"��\r\n");
		jout.write("70~79: "+SC+"��\r\n");
		jout.write("80~89: "+SB+"��\r\n");
		jout.write("90~100: "+SA+"��\r\n");
		jout.close();
		System.out.println("�������");
		System.out.println("��鿴"+"./data_out.txt"+"\n(UTF-8)");
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
package myn;

public class Main {
	public static void main(String[]args){
		Prime test=new Prime(10000000);
		//Prime test=new Prime(100);
		test.printPrime();
		test.printMaxPrime();
	}
}

class Prime{
	public int size;
	public boolean[]pr;
	public Prime(int size){
		this.size=size;
		init();
		setPrime();
	}
	/**
	 * ��ʼ��������Χ�Լ���ʼֵ
	 *@init size 
	 *@init value
	 */
	public void init(){
		pr=new boolean[size+1];
		for (int i=3;i<pr.length;i++) 
			pr[i]=i%2==0?false:true;
		pr[0]=pr[1]=false;
		pr[2]=true;
	}
	public void setSize(int size){
		this.size=size;
		init();
		setPrime();
	}
	/**
	 *��ͨ�����ж�һ�����Ƿ�Ϊ����
	 *@return boolean
	 */
	public static boolean isPrimeBase(int x){
		boolean is=true;
		for(int i=2;i<Math.sqrt(x);i++)
			if(i%x==0){
				is=false;
				break;
			}
		return is;
	}
	/**
	 *��ѯһ�����Ƿ�Ϊ����
	 *@return boolean
	 */
	public boolean isPrime(int x){
		return pr[x];
	}
	/**
	 *����ɸ���������
	 *@return boolean
	 */
	public void setPrime(){
		for(int i=3;i<Math.sqrt(pr.length);i++){
			if(pr[i]){
				for(int j=i*i;j<pr.length;j+=2*i)
					pr[j]=false;
			}
		}
	}
	/**
	 *��ӡ��������
	 */
	public void printPrime(){
		int sum=0;
		for(int i=2;i<pr.length;i++){
			boolean cur=pr[i];
			if(cur&&(++sum)>0)
				System.out.println(i);
		}
		System.out.println("\n��������������"+sum);
		return;	
	}
	/**
	 *�����������
	 *@return Max_Prime
	 */
	public void printMaxPrime(){
		int ans=pr.length-1;
		for(int i=pr.length-1;i>=2;i--){
			boolean cur=pr[i];
			if(cur){
				ans=i;
				break;
			}
		}
		System.out.println("���������"+ans);
		return;	
	}
}

package myn;

public class Main {
	public static void main(String[]args){
		DichoSearch obj = new DichoSearch();
		obj.runSearch();
		double ans=obj.getAns();
		System.out.println(ans);	
	}
}

class DichoSearch{
	/**
	 * @����x
	 * @��ʼ����Сֵ
	 * @��ʼ�����ֵ
	 * @��ȷ��
	 */
	private double x;
	private double x_low;
	private double x_high;
	private double delta;
	/**
	 * ���캯��
	 */
	public DichoSearch(){
		x_low = -10.0;
		x_high = 5.0;
		delta = 0.001;
	}
	/**
	 * ���� f(x) = x^3 -10 x + 23=0��ֵ
	 * @param x 
	 * @return f(x)
	 */
	public double calcVal(double _x){
		double ans=0;;
		ans=Math.pow(_x,3)-10*_x+23;
		return ans;
	}
	/**
	 * �ö��ַ��������̵ĸ�
	 */
	public void runSearch() {
		while(true){
			x=(x_low+x_high)/2;
			if(calcVal(x)*calcVal(x_low)<0)
				x_high=x;
			else
				x_low=x;
			if(calcVal(x)==0||x_high-x_low<delta)
				break;
		}
	}
	/**
	 *  �õ���
	 * @return 
	 */
	public double getAns(){
		return this.x;
	}
}

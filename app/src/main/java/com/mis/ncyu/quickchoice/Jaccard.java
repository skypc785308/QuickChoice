package com.mis.ncyu.quickchoice;

/**
 * Created by UserMe on 2017/9/21.
 */

public class Jaccard {
    private int data[][];
    public Jaccard(int mix[][])
    {
        this.data=mix;
    }
    public Double JaccardCount(int x,int y)//要運算的兩個項目編號(0-N)
    {
        Double andNum=0.0,orNum=0.0;
        int d=data.length;
        Double Jac;
        for(int i=0;i<d;i++)
        {
            if(data[i][x]==2&data[i][y]==2)//求交集數量
            {
                andNum++;
            }
            if(data[i][x]==2|data[i][y]==2)//求連集數量
            {
                orNum++;
            }
        }
        Jac= andNum/orNum;
        return  Jac;
    }
}

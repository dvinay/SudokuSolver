import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;

/*<applet code="SudokuSolver.class"width=380 height=380> </applet> */

public class SudokuSolver extends JApplet implements ActionListener
{
 JTextField cell[][]=new JTextField[10][10];
 int num[][]=new int[10][10];
 int problem[][]=new int[10][10];
 int presentANS[][]=new int[10][10];
 String strNUM[][]=new String[10][10];
 int startX,startY;
 AudioClip ac;
 public void start()
 {
  setLayout(new GridLayout(9,9));
  for(int i=1;i<=9;i++)
  {
   for(int j=1;j<=9;j++)
   {
    cell[i][j]=new JTextField(3);
    cell[i][j].addActionListener(this);
   }
  }
  for(int i=1;i<=9;i++)
   for(int j=1;j<=9;j++)
    add(cell[i][j]);
  for(int i=0;i<=9;i++)
  {
   for(int j=0;j<=9;j++)
   {
    num[i][j]=0;
    presentANS[i][j]=0;
    strNUM[i][j]="0";
    problem[i][j]=0;
   }
  }
  ac=getAudioClip(getDocumentBase(),"sound.wav");
 }
 public void actionPerformed(ActionEvent ae)
 {
  String text;
  ac.loop();
  for(int i=1;i<=9;i++)
  {
   for(int j=1;j<=9;j++)
   {
    text=cell[i][j].getText();
    if(!text.equals(""))
    {
     for(int index=0;index<text.length();index++)
     {
      if(text.charAt(index)!=' ')
      {
       char chNUM[]=new char[1];
       chNUM[0]=text.charAt(index);
       String str=new String(chNUM);
       strNUM[i][j]=str;
       break;
      }
     }
     num[i][j]=Integer.parseInt(strNUM[i][j]);
     problem[i][j]=num[i][j];
     presentANS[i][j]=num[i][j];
    }
   }
  }
   while(!isComplete())
   {
   fillSingles();
   fillHiddenSingles();
   lockedCandidates2();
   fillHiddenSingles();
   lockedCandidates1();
   fillHiddenSingles();
   }
  for(int i=1;i<=9;i++)
  {
   for(int j=1;j<=9;j++)
   {
    cell[i][j].setFont(new Font("ScansCerif",Font.BOLD,14));
    if(presentANS[i][j]==problem[i][j])
     cell[i][j].setBackground(Color.lightGray);
    cell[i][j].setText("    "+presentANS[i][j]);
    cell[i][j].setEditable(false);
   }
  }
 }
 public void stop()
 {
  ac.stop();
 }
 void reset()
 {
  for(int i=0;i<=9;i++)
  {
   for(int j=0;j<=9;j++)
   {
    num[i][j]=0;
    strNUM[i][j]="";
   }
  }
 }
 boolean isComplete()
 {
  boolean isOver=false;
  for(int i=1;i<=9;i++)
  {
   for(int j=1;j<=9;j++)
   {
    if(presentANS[i][j]>0)
     isOver=true;
    else
    {
     isOver=false;
     return isOver;
    }
   }
  }
  return isOver;
 }
 int genPossibleValue(int i,int j)
 {
  int source[]={0,1,2,3,4,5,6,7,8,9};
  int possiblenum=0;
  if(presentANS[i][j]>0)
   return presentANS[i][j];
  for(int k=1;k<=9;k++)
  {
   if(presentANS[i][k]>0)
   {
    int l=presentANS[i][k];
    source[l]=0;
   }
  }
  for(int k=1;k<=9;k++)
  {
   if(presentANS[k][j]>0)
   {
    int l=presentANS[k][j];
    source[l]=0;
   }
  }
  retSxandSy(i,j);
  for(int m=startX;m<=startX+2;m++)
  {
   for(int n=startY;n<=startY+2;n++)
   {
    if(presentANS[m][n]>0)
    {
     int l=presentANS[m][n];
     source[l]=0;
    }
   }
  }
  for(int m=1;m<=9;m++)
  {
   if(source[m]!=0)
   possiblenum=(possiblenum*10)+source[m];
  }
  return possiblenum;  
 }
 void retSxandSy(int i,int j)
 {
  if(i>=1&&i<=3)
   startX=1;
  else if(i>=4&&i<=6)
   startX=4;
  else
   startX=7;
  if(j>=1&&j<=3)
   startY=1;
  else if(j>=4&&j<=6)
   startY=4;
  else
   startY=7;
 } 
 void fillSingles()
 {
  for(int i=1;i<=9;i++)
  {
   for(int j=1;j<=9;j++)
   {
    num[i][j]=genPossibleValue(i,j);
    if(num[i][j]>9)
    {
     strNUM[i][j]=Integer.toString(num[i][j]);
    }                  
    else
    {
     if(presentANS[i][j]==0)
     {
      presentANS[i][j]=num[i][j];
      reset();
      fillSingles();
     }
    }
   }
  }
 }
 void fillHiddenSingles()
 {
  for(int i=1;i<=9;i++)         //HIDDEN SINGLES IN ROWS
  {
   String rowString="";
   for(int j=1;j<=9;j++)
    if(num[i][j]>9)
     rowString+=strNUM[i][j];
   for(int k=1;k<=9;k++)
   {
    String text="";
    text+=k;
    int rFRQ=retNoOfTimes(rowString,text);
    if(rFRQ==1)
    {
     for(int m=1;m<=9;m++)
     {
      if(strNUM[i][m].contains(text))
      {
       int value=Integer.parseInt(text);
       presentANS[i][m]=value;
       reset();
       fillSingles();
       i=1;
       m=10;
       k=10;
      }
     }
    }
   }
  }                             //END OF ROW
  if(isComplete())
    return;
  for(int i=1;i<=9;i++)        //HIDDEN SINGLES IN COLUMNS
  {
   String columnString="";
   for(int j=1;j<=9;j++)
    if(num[j][i]>9)
     columnString+=strNUM[j][i];
   for(int k=1;k<=9;k++)
   {
    String text="";
    text+=k;
    int cFRQ=retNoOfTimes(columnString,text);
    if(cFRQ==1)
    {
     for(int m=1;m<=9;m++)
     {
      if(strNUM[m][i].contains(text))
      {
       int value=Integer.parseInt(text);
       presentANS[m][i]=value;
       reset();
       fillSingles();
       i=1;
       m=10;
       k=10;
      }
     }
    }
   }
  }                       //END OF COLUMNS
  if(isComplete())
   return ;
  for(int i=1;i<=9;i=i+3)   //HODDEN SINGLES IN BOXES
  {
   for(int j=1;j<=9;j=j+3)
   {
    String boxString="";
    for(int m=i;m<=i+2;m++)
     for(int n=j;n<=j+2;n++)
       if(num[m][n]>9)
        boxString+=strNUM[m][n];
    for(int k=1;k<=9;k++)
    {
     String text="";
     text+=k;
     int bFRQ=retNoOfTimes(boxString,text);
     if(bFRQ==1)
     {
      for(int m=i;m<=i+2;m++)
      {
       for(int n=j;n<=j+2;n++)
       {
        if(strNUM[m][n].contains(text))
        {
         int value=Integer.parseInt(text);
         presentANS[m][n]=value;
         reset();
         fillSingles();
         i=1;
         j=1;
        }
       }
      }
     }
    }
   }
  }             //END IF BOXES
 }
 int retNoOfTimes(String str,String text)
 {
  char ch=text.charAt(0);
  int count=0;
  for(int i=0;i<str.length();i++)
  {
   if(ch==str.charAt(i))
    count+=1;
  }
  return count;
 }
 void lockedCandidates1()
 {
  for(int i=1;i<=9;i=i+3)
  {
   first:for(int j=1;j<=9;j=j+3)
   {
    String boxString="";
    for(int m=i;m<=i+2;m++)
     for(int n=j;n<=j+2;n++)
     {
       if(num[m][n]>9)
        boxString+=strNUM[m][n];
     }
    for(int k=1;k<=9;k++)
    {
     String text="";
     text+=k;
     int bFRQ=retNoOfTimes(boxString,text);
     if(bFRQ==2)
     {
      int r1=10,r2=12,c1=13,c2=14,count=1;
      for(int m=i;m<=i+2;m++)
      {
       for(int n=j;n<=j+2;n++)
       {
        if(strNUM[m][n].contains(text))
        {
         if(count==1)
         {
          r1=m;
          c1=n;
          count+=1;
         }
         else
         {
          r2=m;
          c2=n;
         }
        }
       }
      }
      if(r1==r2)
      {
       int lBV,hBV;
       if(c1%3==1)
        lBV=c1;
       else
        lBV=c1-1;
       if(c2%3==0)
        hBV=c2;
       else
        hBV=c2+1;
       for(int p=1;p<=9;p++)
       {
        if(p>=lBV&&p<=hBV)
        {
        }
        else
        {
         if(strNUM[r1][p].contains(text))
         {
          char charStr[]=new char[strNUM[r1][p].length()-1];
          char val=text.charAt(0);
          int index=0;
          for(int x=0;x<strNUM[r1][p].length();x++)
          {
           if(strNUM[r1][p].charAt(x)!=val)
           {
            charStr[index]=strNUM[r1][p].charAt(x);
            index+=1;
           }
          }
          strNUM[r1][p]=new String(charStr);
          num[r1][p]=Integer.parseInt(strNUM[r1][p]);
           fillHiddenSingles();
           i=-2;
           break first;
          }
         }
        }
       }
      if(c1==c2)
      {
       int lBV,hBV;
       if(r1%3==1)
        lBV=r1;
       else
        lBV=r1-1;
       if(r2%3==0)
        hBV=r2;
       else
        hBV=r2+1;
       for(int p=1;p<=9;p++)
       {
        if(p>=lBV&&p<=hBV)
        {
        }
        else
        {
         if(strNUM[p][c1].contains(text))
         {
          char charStr[]=new char[strNUM[p][c1].length()-1];
          char val=text.charAt(0);
          int index=0;
          for(int x=0;x<strNUM[p][c1].length();x++)
          {
           if(strNUM[p][c1].charAt(x)!=val)
           {
            charStr[index]=strNUM[p][c1].charAt(x);
            index+=1;
           }
          }
          strNUM[p][c1]=new String(charStr);
          num[p][c1]=Integer.parseInt(strNUM[p][c1]);
          fillHiddenSingles();
          i=-2;
          break first;
         }
        }
       }
      }
     }
     if(bFRQ==3)
     {
      int r1=10,r2=12,c1=13,c2=14,r3=15,c3=16,count=1;
      for(int m=i;m<=i+2;m++)
      {
       for(int n=j;n<=j+2;n++)
       {
        if(strNUM[m][n].contains(text))
        {
         if(count==1)
         {
          r1=m;
          c1=n;
          count+=1;
         }
         else if(count==2)
         {
          r2=m;
          c2=n;
          count+=1;
         }
         else
         {
          r3=m;
          c3=n;
         }
        }
       }
      }
      if(r1==r2&&r1==r3)
      {
       int lBV,hBV;
        lBV=c1;
        hBV=c3;
       for(int p=1;p<=9;p++)
       {
        if(p>=lBV&&p<=hBV)
        {
        }
        else
        {
         if(strNUM[r1][p].contains(text))
         {
          char charStr[]=new char[strNUM[r1][p].length()-1];
          char val=text.charAt(0);
          int index=0;
          for(int x=0;x<strNUM[r1][p].length();x++)
          {
           if(strNUM[r1][p].charAt(x)!=val)
           {
            charStr[index]=strNUM[r1][p].charAt(x);
            index+=1;
           }
          }
          strNUM[r1][p]=new String(charStr);
          num[r1][p]=Integer.parseInt(strNUM[r1][p]);
           fillHiddenSingles();
           i=-2;
           break first;
          }
         }
        }
       }
      if(c1==c2&&c1==c3)
      {
       int lBV,hBV;
        lBV=r1;
        hBV=r3;
       for(int p=1;p<=9;p++)
       {
        if(p>=lBV&&p<=hBV)
        {
        }
        else
        {
         if(strNUM[p][c1].contains(text))
         {
          char charStr[]=new char[strNUM[p][c1].length()-1];
          char val=text.charAt(0);
          int index=0;
          for(int x=0;x<strNUM[p][c1].length();x++)
          {
           if(strNUM[p][c1].charAt(x)!=val)
           {
            charStr[index]=strNUM[p][c1].charAt(x);
            index+=1;
           }
          }
          strNUM[p][c1]=new String(charStr);
          num[p][c1]=Integer.parseInt(strNUM[p][c1]);
          fillHiddenSingles();
          i=-2;
          break first;
         }
        }
       }
      }
     }
    }
   }
  }
 }
 void lockedCandidates2()
 {
  for(int i=1;i<=9;i++)         //LOCKED CANDIDATES2 IN ROWS
  {
   String rowString="";
   for(int j=1;j<=9;j++)
    if(num[i][j]>9)
     rowString+=strNUM[i][j];
   repet:for(int k=1;k<=9;k++)
   {
    String text="";
    text+=k;
    int rFRQ=retNoOfTimes(rowString,text);
    if(rFRQ==2)
    {
     int c1=1,c2=2,count=1;
     for(int m=1;m<=9;m++)
     {
      if(strNUM[i][m].contains(text))
      {
       if(count==1)
       {
        c1=m;
        count+=1;
       }
       else
        c2=m;
      }
     }
     if(c2-c1<3)
     {
      int rem=(c2+c1)%6;
      if(rem==3||rem==4||rem==5)
      {
       retSxandSy(i,c1);
       for(int p=startX;p<=startX+2;p++)
       {
        for(int q=startY;q<=startY+2;q++)
        {
         if(p==i)
         {
         }
         else
         {
          if(strNUM[p][q].contains(text))
          { 
           char charStr[]=new char[strNUM[p][q].length()-1];
           char val=text.charAt(0);
           int index=0;
           for(int x=0;x<strNUM[p][q].length();x++)
           {
            if(strNUM[p][q].charAt(x)!=val)
            {
             charStr[index]=strNUM[p][q].charAt(x);
             index+=1;
            }
           }
           strNUM[p][q]=new String(charStr);
           num[p][q]=Integer.parseInt(strNUM[p][q]);
           fillHiddenSingles();
           i=0;
           break repet;
          }
         }
        }
       }  
      }
     }
    }
    if(rFRQ==3)
    {
     int c1=10,c2=12,c3=14,count=1;
     for(int m=1;m<=9;m++)
     {
      if(strNUM[i][m].contains(text))
      {
       if(count==1)
       {
        c1=m;
        count+=1;
       }
       else if(count==2)
       {
        c2=m;
        count+=1;
       }
       else
        c3=m;
      }
     }
     if(c1%3==0&&c2%3==1&&c3%3==2)
     {
       retSxandSy(i,c1);
       for(int p=startX;p<=startX+2;p++)
       {
        for(int q=startY;q<=startY+2;q++)
        {
         if(p==i)
         {
         }
         else
         {
          if(strNUM[p][q].contains(text))
          { 
           char charStr[]=new char[strNUM[p][q].length()-1];
           char val=text.charAt(0);
           int index=0;
           for(int x=0;x<strNUM[p][q].length();x++)
           {
            if(strNUM[p][q].charAt(x)!=val)
            {
             charStr[index]=strNUM[p][q].charAt(x);
             index+=1;
            }
           }
           strNUM[p][q]=new String(charStr);
           num[p][q]=Integer.parseInt(strNUM[p][q]);
           fillHiddenSingles();
           i=0;
           break repet;
          }
         }
        }
       }  
      
     }
    }
   }
  }
  for(int i=1;i<=9;i++)         //LOCKED CANDIDATES2 IN columns
  {
   String columnString="";
   for(int j=1;j<=9;j++)
    if(num[i][j]>9)
     columnString+=strNUM[j][i];
   repet:for(int k=1;k<=9;k++)
   {
    String text="";
    text+=k;
    int rFRQ=retNoOfTimes(columnString,text);
    if(rFRQ==2)
    {
     int r1=1,r2=2,count=1;
     for(int m=1;m<=9;m++)
     {
      if(strNUM[m][i].contains(text))
      {
       if(count==1)
       {
        r1=m;
        count+=1;
       }
       else
        r2=m;
      }
     }
     if(r2-r1<3)
     {
      int rem=(r2+r1)%6;
      if(rem==3||rem==4||rem==5)
      {
       retSxandSy(i,r1);
       for(int p=startX;p<=startX+2;p++)
       {
        for(int q=startY;q<=startY+2;q++)
        {
         if(p==i)
         {
         }
         else
         {
          if(strNUM[p][q].contains(text))
          { 
           char charStr[]=new char[strNUM[p][q].length()-1];
           char val=text.charAt(0);
           int index=0;
           for(int x=0;x<strNUM[p][q].length();x++)
           {
            if(strNUM[p][q].charAt(x)!=val)
            {
             charStr[index]=strNUM[p][q].charAt(x);
             index+=1;
            }
           }
           strNUM[p][q]=new String(charStr);
           num[p][q]=Integer.parseInt(strNUM[p][q]);
           fillHiddenSingles();
           i=0;
           break repet;
          }
         }
        }
       }  
      }
     }
    }
    if(rFRQ==3)
    {
     int r1=11,r2=12,r3=13,count=1;
     for(int m=1;m<=9;m++)
     {
      if(strNUM[m][i].contains(text))
      {
       if(count==1)
       {
        r1=m;
        count+=1;
       }
       else if(count==2)
       {
        r2=m;
        count+=1;
       }
       else
        r3=m;
      }
     }
     if(r1%3==0&&r2%3==1&&r3%3==2)
     {
       retSxandSy(i,r1);
       for(int p=startX;p<=startX+2;p++)
       {
        for(int q=startY;q<=startY+2;q++)
        {
         if(p==i)
         {
         }
         else
         {
          if(strNUM[p][q].contains(text))
          { 
           char charStr[]=new char[strNUM[p][q].length()-1];
           char val=text.charAt(0);
           int index=0;
           for(int x=0;x<strNUM[p][q].length();x++)
           {
            if(strNUM[p][q].charAt(x)!=val)
            {
             charStr[index]=strNUM[p][q].charAt(x);
             index+=1;
            }
           }
           strNUM[p][q]=new String(charStr);
           num[p][q]=Integer.parseInt(strNUM[p][q]);
           fillHiddenSingles();
           i=0;
           break repet;
          }
         }
        }
       }  
      
     }
    }
   }
  }
 }
}

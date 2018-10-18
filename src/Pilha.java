/**
 *
 * @author Otavio
 */

public class Pilha {
   private int tamMax;
   private String[] pilhaArray;
   private int top;
   
   public Pilha(int s) {
      tamMax = s;
      pilhaArray = new String[tamMax];
      top = -1;
   }
   public void push(String j) {
      pilhaArray[++top] = j;
   }
   public String pop() {
      return pilhaArray[top--];
   }
   public boolean taVazia() {
      return (top == -1);
   }
   public boolean taCheia() {
      return (top == tamMax - 1);
   }
   /*
   public static void main(String[] args) {
      Pilha theStack = new Pilha(10); 
      theStack.push(10);
      theStack.push(20);
      theStack.push(30);
      theStack.push(40);
      theStack.push(50);
      
      while (!theStack.isEmpty()) {
         long value = theStack.pop();
         System.out.print(value);
         System.out.print(" ");
      }
      System.out.println("");
   }*/
}

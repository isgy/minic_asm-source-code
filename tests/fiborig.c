/*
 * =====================================================================================
 *
 *       Filename:  fiborig.c
 *
 *    Description:  
 *
 *        Version:  1.0
 *        Created:  24/10/17 16:46:29
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  YOUR NAME (), 
 *   Organization:  
 *
 * =====================================================================================
 */
#include "foo"
   struct database {
  int id_number;
  int age;
  int* salary;
};


void main() {
  int n;
  int first;
  int second;
  int next;
  int c;
  char t;

  // read n from the standard input
  n = read_i();
  
  first = 0;
  second = 1;
 next = t;   
  print_s((char*)"First ");
  print_i(n);
  print_s((char*)" terms of Fibonacci series are : "); 
  if(n < c){
     second = c;
  c = 0;
  while (c < n) {
    if ( c <= 1 )
      next = c;
    else
      {
	next = first + second;
	first = second;
	second = next;
      }
    print_i(next);
    print_s((char*)" ");
    c = c+1;
  }
}
}

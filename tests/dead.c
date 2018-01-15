/*
 * =====================================================================================
 *
 *       Filename:  dead.c
 *
 *    Description:  
 *
 *        Version:  1.0
 *        Created:  09/01/18 21:29:09
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  YOUR NAME (), 
 *   Organization:  
 *
 * =====================================================================================
 */
#include <stdlib.h>

int main()
 {
   int a = 24;
   int b = 25; /* Assignment to dead variable */
   int c;
   c = a * 4;
   return c;
   b = 24; /* Unreachable code */
   return 0;
 }

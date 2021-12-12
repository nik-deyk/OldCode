#include <stdio.h>
#include <string.h>
//in functions.asm
double f1(double x);
double f2(double x);
double f3(double x);
//in boarders.asm
double f1_f2(int n);
double f1_f3(int n);
double f2_f3(int n);
//in extendcode.c
double root(double(*f1)(double), double(*f2)(double), double a, double b, double eps);
double integral(double(*f)(double), double a, double b, double eps2);
int get_cnt(void);
double dabs(double x);
//in cmdcode.c
int cmd(int argc, char **argv);
int get_mode(void);
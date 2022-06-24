#include <math.h>

static int count = 0;

double root(double(*f1)(double), double(*f2)(double), double a, double b, double eps) {
	if ((f1(a) - f2(a)) * (f1(b) - f2(b)) > 0) {
		return INFINITY;
	}

	count++;
	double c = (a + b) / 2;
	if (b - a < eps) {
		return c;
	}

	if ((f1(c) - f2(c)) * (f1(b) - f2(b)) < 0) {
		return root(f1, f2, c, b, eps);
	}

	return root(f1, f2, a, c, eps);
}

double integral(double(*f)(double), double a, double b, double eps2) {
	int n = (int)round(pow((b - a), 5)/(288*eps2));
	if (n == 0) {
		n++;
	}
	double h = (b - a)/(2*n);
	double s = f(a);

	double t = 0;
	for (double x = (a + h); x < b; x += 2*h) {
		t += f(x);
	}
	s += (4*t);

	t = 0;
	for (double x = (a + 2*h); (x + h) < b; x += 2*h) {
		t += f(x);
	}
	s += (2*t);

	s += f(b);

	s = s*(h/3.0);

	return s;
}

int get_cnt(void) {
	int t = count;
	count = 0;
	return t;
}

double dabs(double x) {
	return (x > 0) ? x : (-x);
}
/* 
double str2double(char *str) {
}

int strcheck(char *str) {
	int i = 0;

	if (((str[i] < '0') || (str[i] > '9')) && (str[i] != '-')) {
		return -2;
	}

	for (i++; str[i]; i++) {
		if (((str[i] < '0') || (str[i] > '9')) && (str[i] != '.')) {
			return -2;
		}

		if (str[i] == '.') {
			break;
		}
	}

	int point = i;

	if (str[i] == '.') {
		for (i++; str[i]; i++) {
			if (((str[i] < '0') || (str[i] > '9')) && (str[i] != '.')) {
				return -2;
			}
		}
	}

	return point;
} */
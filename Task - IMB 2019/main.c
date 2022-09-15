#include "main.h"

#define eps1 0.001
#define eps2 0.001

int main(int argc, char **argv) {
	int code = cmd(argc, argv);
	if (!code) {
		return 0;
	}
	
	int mymode = get_mode();

	double rootf1_f2 = root(f1, f2, f1_f2(0), f1_f2(1), eps1);
	int cnt = get_cnt();
	if (mymode & 1) {
		printf("Iterations f1 & f2: %d\n", cnt);
	}
	if (mymode & 2) {
		printf("Root of f1 & f2: %lf\n", rootf1_f2);
	}

	double rootf2_f3 = root(f2, f3, f2_f3(0), f2_f3(1), eps1);
	cnt = get_cnt();
	if (mymode & 1) {
		printf("Iterations f2 & f3: %d\n", cnt);
	}
	if (mymode & 2) {
		printf("Root of f2 & f3: %lf\n", rootf2_f3);
	}

	double rootf1_f3 = root(f1, f3, f1_f3(0), f1_f3(1), eps1);
	cnt = get_cnt();
	if (mymode & 1) {
		printf("Iterations f1 & f3: %d\n", cnt);
	}
	if (mymode & 2) {
		printf("Root of f1 & f3: %lf\n", rootf1_f3);
	}

	double r;
	if ((rootf1_f2 < rootf1_f3) && (rootf1_f3 < rootf2_f3)) {
		r = integral(f1, rootf1_f2, rootf1_f3, eps2);
		r += integral(f3, rootf1_f3, rootf2_f3, eps2);
		r -= integral(f2, rootf1_f2, rootf2_f3, eps2);
	} else if ((rootf1_f2 < rootf2_f3) && (rootf2_f3 < rootf1_f3)) {
		r = integral(f2, rootf1_f2, rootf2_f3, eps2);
		r += integral(f3, rootf2_f3, rootf1_f3, eps2);
		r -= integral(f1, rootf1_f2, rootf1_f3, eps2);
	} else if ((rootf2_f3 < rootf1_f3) && (rootf1_f3 < rootf1_f2)) {
		r = integral(f3, rootf2_f3, rootf1_f3, eps2);
		r += integral(f1, rootf1_f3, rootf1_f2, eps2);
		r -= integral(f2, rootf2_f3, rootf1_f2, eps2);
	} else if ((rootf2_f3 < rootf1_f2) && (rootf1_f2 < rootf1_f3)) {
		r = integral(f2, rootf2_f3, rootf1_f2, eps2);
		r += integral(f1, rootf1_f2, rootf1_f3, eps2);
		r -= integral(f3, rootf2_f3, rootf1_f3, eps2);
	} else if ((rootf1_f3 < rootf2_f3) && (rootf2_f3 < rootf1_f2)) {
		r = integral(f3, rootf1_f3, rootf2_f3, eps2);
		r += integral(f2, rootf2_f3, rootf1_f2, eps2);
		r -= integral(f1, rootf1_f3, rootf1_f2, eps2);
	} else /*if ((rootf1_f3 < rootf1_f2) && (rootf1_f2 < rootf2_f3))*/ {
		r = integral(f1, rootf1_f3, rootf1_f2, eps2);
		r += integral(f2, rootf1_f2, rootf2_f3, eps2);
		r -= integral(f3, rootf1_f3, rootf2_f3, eps2);
	}

	if (r < 0) {
		r *= (-1);
	}

	printf("Result: %lf\n", r);

	return 0;
}
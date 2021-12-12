#include "main.h"

#define version "1.0.0.0"

static int mode = 0;

int cmd(int argc, char **argv) {
	for (int i = 1; i < argc; i++) {
		if (!strcmp(argv[i], "-help")) {
			printf("Alex' Poklonsky program. Allowed comands:\n");
			printf("\t-help    return this information\n");
			printf("\t-info    return the name of program and version\n");
			printf("\t-root    test the function 'root'\n");
			printf("\t-int     test the function 'integral'\n");
			printf("\t-cnt     if the count of iterations while finding the root should be returned to output\n");
			printf("\t-x       if the found roots should be returned to output\n");
			printf("If you want to use last two keys with '-root', please, place them before '-root'\n");
			printf("Use the '.' symbol to input double numbers\n");

			return 0;
		} 
		
		if (!strcmp(argv[i], "-info")) {
			printf("Program' name: %s\n", argv[0]);
			printf("Current version: %s\n", version);

			return 0;
		}

		if (!strcmp(argv[i], "-cnt") && !(mode & 1)) {
			mode |= 1;

			continue;
		}

		if (!strcmp(argv[i], "-x") && !(mode & 2)) {
			mode |= 2;

			continue;
		}

		if (!strcmp(argv[i], "-root")) {
			printf("input first f: ");
			int c;
			scanf("%d", &c);
			while (c < 1 || c > 3) {
				printf("Wrong. Try again: ");
				scanf("%d", &c);
			}

			printf("input second f: ");
			int c2;
			scanf("%d", &c2);
			while (c2 < 1 || c2 > 3) {
				printf("Wrong. Try again: ");
				scanf("%d", &c2);
			}

			if (c == c2) {
				printf("Bad input\n");
				return 0;
			}

			double a, b, customeps;
			printf("Input boarders of the cut: ");
			scanf("%lf %lf", &a, &b);
			printf("Input exactness: ");
			scanf("%lf", &customeps);

			double (*cf1)(double);
			switch (c)
			{
			case 1:
				cf1 = f1;
				break;

			case 2:
				cf1 = f2;
				break;
			
			default:
				cf1 = f3;
				break;
			}

			double (*cf2)(double);
			switch (c2)
			{
			case 1:
				cf2 = f1;
				break;

			case 2:
				cf2 = f2;
				break;
			
			default:
				cf2 = f3;
				break;
			}

			double r = root(cf1, cf2, a, b, customeps);
			int cnt = get_cnt();
			printf("result: %lf\n", r);
			if (mode & 1) {
				printf("Count of iterations: %d\n", cnt);
			}

			return 0;
		}

		if (!strcmp(argv[i], "-int")) {
			printf("input first f: ");
			int c;
			scanf("%d", &c);
			while (c < 1 || c > 3) {
				printf("Wrong. Try again: ");
				scanf("%d", &c);
			}

			double a, b, customeps;
			printf("Input boarders of the cut: ");
			scanf("%lf %lf", &a, &b);
			printf("Input exactness: ");
			scanf("%lf", &customeps);

			double (*cf1)(double);
			switch (c)
			{
			case 1:
				cf1 = f1;
				break;

			case 2:
				cf1 = f2;
				break;
			
			default:
				cf1 = f3;
				break;
			}

			double r = integral(cf1, a, b, customeps);
			printf("result: %lf\n", r);

			return 0;
		}

		printf("Bad keys\n");
		return 0;
	}

	return 1;
}

int get_mode(void) {
	return mode;
}
s = 

default: program
	@ echo ...running...
	@ ./program $(s)

all program: main.o extendcode.o cmdcode.o functions.o boarders.o 
	@ echo ...linking...
	@ gcc -m32 -o program main.o extendcode.o cmdcode.o functions.o boarders.o -lm

main.o: main.c
	@ echo ...compiling c-source...
	@ gcc -m32 -c -o main.o main.c

extendcode.o: extendcode.c
	@ echo ...compiling c-source...
	@ gcc -m32 -c -o extendcode.o extendcode.c

cmdcode.o: cmdcode.c
	@ echo ...compiling c-source...
	@ gcc -m32 -c -o cmdcode.o cmdcode.c

functions.o: functions.asm
	@ echo ...compiling nasm-source...
	@ nasm -f elf32 -o functions.o functions.asm

boarders.o: boarders.asm
	@ echo ...compiling nasm-source...
	@ nasm -f elf32 -o boarders.o boarders.asm

clear:
	@ echo ...removing...
	@ rm *.o
	@ rm program
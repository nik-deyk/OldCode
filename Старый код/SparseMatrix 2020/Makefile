COMPILER ::= -O2 -Wall -Werror -Wformat-security -Wignored-qualifiers \
	-Winit-self -Wswitch-default -Wfloat-equal -Wshadow -Wpointer-arith \
	-Wtype-limits -Wempty-body -Wlogical-op -Wmissing-field-initializers \
	-Wctor-dtor-privacy  -Wnon-virtual-dtor -Wstrict-null-sentinel  \
	-Wold-style-cast -Woverloaded-virtual -Wsign-promo -Weffc++ -std=gnu++0x -m32

all run : $(N) 
	./$(N) $(COM)

build $(N): $(N).cpp
	@ g++ $(N).cpp -o $(N) $(COMPILER)

clean:
	@ find . -type f -not \( -name 'Makefile' -or -name '*.*' \) -delete
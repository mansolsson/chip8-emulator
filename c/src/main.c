#include <stdio.h>
#include <stdlib.h>
#include <SDL2/SDL.h>
#include "emulator.h"

int main(int argc, char **argv)
{
	if(argc != 2) {
		fprintf(stderr, "usage: %s <path-to-program>\n", argv[0]);
		exit(1);
	}
	start_program(argv[1]);
	return 0;
}

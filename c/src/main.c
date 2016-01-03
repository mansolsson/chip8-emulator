#include <stdio.h>
#include <stdlib.h>
#include <SDL2/SDL.h>
#include "emulator.h"

void print_usage(char *program);

int main(int argc, char **argv)
{
	if(argc != 2) {
		print_usage(argv[0]);
	}
	start_program(argv[1]);
	return 0;
}

void print_usage(char *program)
{
	fprintf(stderr, "usage: %s <path-to-program>\n", program);
	exit(1);
}
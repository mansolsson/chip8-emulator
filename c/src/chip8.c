#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include "chip8.h"

void init_chip8(struct chip8 *c)
{
	for(uint32_t i = 0; i < SCREEN_SIZE; i++) {
		c->screen[i] = false;
	}
	for(uint32_t i = 0; i < NR_KEYS; i++) {
		c->keys[i] = false;
	}
	init_memory(c);
	c->pc = PROGRAM_START;
	c->stack_index = 0;
	c->sound_timer = 0;
	c->delay_timer = 0;
}

void init_memory(struct chip8 *c) 
{
	for(uint32_t i = 0; i < MEMORY_SIZE; ++i) {
		c->memory[i] = 0;
	}
	// 0
	c->memory[0] = 0b01100000;
	c->memory[1] = 0b10010000;
	c->memory[2] = 0b10010000;
	c->memory[3] = 0b10010000;
	c->memory[4] = 0b01100000;
	// 1
	c->memory[5] = 0b00010000;
	c->memory[6] = 0b00010000;
	c->memory[7] = 0b00010000;
	c->memory[8] = 0b00010000;
	c->memory[9] = 0b00010000;
	// 2
	c->memory[10] = 0b11110000;
	c->memory[11] = 0b00010000;
	c->memory[12] = 0b11110000;
	c->memory[13] = 0b10000000;
	c->memory[14] = 0b11110000;
	// 3
	c->memory[15] = 0b11110000;
	c->memory[16] = 0b00010000;
	c->memory[17] = 0b01110000;
	c->memory[18] = 0b00010000;
	c->memory[19] = 0b11110000;
	// 4
	c->memory[20] = 0b10010000;
	c->memory[21] = 0b10010000;
	c->memory[22] = 0b11110000;
	c->memory[23] = 0b00010000;
	c->memory[24] = 0b00010000;
	// 5
	c->memory[25] = 0b11110000;
	c->memory[26] = 0b10000000;
	c->memory[27] = 0b11110000;
	c->memory[28] = 0b00010000;
	c->memory[29] = 0b11110000;
	// 6
	c->memory[30] = 0b11110000;
	c->memory[31] = 0b10000000;
	c->memory[32] = 0b11110000;
	c->memory[33] = 0b10010000;
	c->memory[34] = 0b11110000;
	// 7
	c->memory[35] = 0b11110000;
	c->memory[36] = 0b00010000;
	c->memory[37] = 0b00010000;
	c->memory[38] = 0b00010000;
	c->memory[39] = 0b00010000;
	// 8
	c->memory[40] = 0b11110000;
	c->memory[41] = 0b10010000;
	c->memory[42] = 0b11110000;
	c->memory[43] = 0b10010000;
	c->memory[44] = 0b11110000;
	// 9
	c->memory[45] = 0b11110000;
	c->memory[46] = 0b10010000;
	c->memory[47] = 0b11110000;
	c->memory[48] = 0b00010000;
	c->memory[49] = 0b00010000;
	// A
	c->memory[50] = 0b01100000;
	c->memory[51] = 0b10010000;
	c->memory[52] = 0b11110000;
	c->memory[53] = 0b10010000;
	c->memory[54] = 0b10010000;
	// B
	c->memory[55] = 0b11100000;
	c->memory[56] = 0b10010000;
	c->memory[57] = 0b11100000;
	c->memory[58] = 0b10010000;
	c->memory[59] = 0b11100000;
	// C
	c->memory[60] = 0b01110000;
	c->memory[61] = 0b10000000;
	c->memory[62] = 0b10000000;
	c->memory[63] = 0b10000000;
	c->memory[64] = 0b01110000;
	// D
	c->memory[65] = 0b11100000;
	c->memory[66] = 0b10010000;
	c->memory[67] = 0b10010000;
	c->memory[68] = 0b10010000;
	c->memory[69] = 0b11100000;
	// E
	c->memory[70] = 0b11110000;
	c->memory[71] = 0b10000000;
	c->memory[72] = 0b11100000;
	c->memory[73] = 0b10000000;
	c->memory[74] = 0b11110000;
	// F
	c->memory[75] = 0b11110000;
	c->memory[76] = 0b10000000;
	c->memory[77] = 0b11100000;
	c->memory[78] = 0b10000000;
	c->memory[79] = 0b10000000;
}

void load_program(struct chip8 *c, char *path)
{
	init_chip8(c);
	FILE *fp;
	if((fp = fopen(path, "r")) == NULL) {
		fprintf(stderr, "File not found: %s\n", path);
		exit(1);
	}
	fread(c->memory + PROGRAM_START, sizeof(uint8_t), MEMORY_SIZE - PROGRAM_START, fp);
	fclose(fp);
}

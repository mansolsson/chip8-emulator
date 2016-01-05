#ifndef CHIP8_H
#define CHIP8_H

#include <stdbool.h>
#include <stdint.h>

#define SCREEN_SIZE 64 * 32
#define STACK_SIZE 16
#define BYTES_PER_INSTRUCTION 2
#define MEMORY_SIZE 4096
#define PROGRAM_START 512
#define NR_REGISTERS 16
#define NR_KEYS 16

struct chip8 {
	bool screen[SCREEN_SIZE];
	uint16_t stack[STACK_SIZE];
	char stack_index;
	uint16_t pc;

	uint8_t memory[MEMORY_SIZE];
	uint8_t registers[NR_REGISTERS];
	uint16_t address_register;
	bool keys[NR_KEYS];
	uint8_t sound_timer;
	uint8_t delay_timer;
};

void init_chip8(struct chip8 *c);
void init_memory(struct chip8 *c);
void execute_opcode(struct chip8 *c, uint16_t opcode);
void handle_unknown_opcode(uint16_t opcode);
void clear_screen(struct chip8 *c);
void return_from_subroutine(struct chip8 *c);
void jump(struct chip8 *c, uint16_t address);
void call_subroutine(struct chip8 *c, uint16_t address);
void draw_sprite(struct chip8 *c, int screen_x, int screen_y, int rows);
void load_program(struct chip8 *c, char *path);

#endif

#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>
#include <stdio.h>
#include "cpu.h"
#include "chip8.h"

void execute_opcode(struct chip8 *c, uint16_t opcode)
{
	uint32_t i;
	uint8_t org_value;
	bool key_pressed;

	switch(opcode & 0xF000) {
	case 0x0000:
		switch(opcode) {
		case 0x00E0:
			clear_screen(c);
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x00EE:
			return_from_subroutine(c);
			break;
		default:
			handle_unknown_opcode(opcode);
		}
		break;
	case 0x1000:
		jump(c, opcode & 0x0FFF);
		break;
	case 0x2000:
		call_subroutine(c, opcode & 0x0FFF);
		break;
	case 0x3000:
		if(c->registers[(opcode & 0x0F00) >> 8] == (opcode & 0x00FF)) {
			c->pc += BYTES_PER_INSTRUCTION;
		}
		c->pc += BYTES_PER_INSTRUCTION;
		break;
	case 0x4000:
		if(c->registers[(opcode & 0x0F00) >> 8] != (opcode & 0x00FF)) {
			c->pc += BYTES_PER_INSTRUCTION;
		}
		c->pc += BYTES_PER_INSTRUCTION;
		break;
	case 0x5000:
		if(c->registers[(opcode & 0x0F00) >> 8] == c->registers[(opcode & 0x00F0) >> 4]) {
			c->pc += BYTES_PER_INSTRUCTION;
		}
		c->pc += BYTES_PER_INSTRUCTION;
		break;
	case 0x6000:
		c->registers[(opcode & 0x0F00) >> 8] = opcode & 0x00FF;
		c->pc += BYTES_PER_INSTRUCTION;
		break;
	case 0x7000:
		c->registers[(opcode & 0x0F00) >> 8] += opcode & 0x00FF;
		c->pc += BYTES_PER_INSTRUCTION;
		break;
	case 0x8000:
		switch(opcode & 0xF) {
		case 0x0:
			c->registers[(opcode & 0x0F00) >> 8] = c->registers[(opcode & 0x00F0) >> 4];
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x1:
			c->registers[(opcode & 0x0F00) >> 8] |= c->registers[(opcode & 0x00F0) >> 4];
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x2:
			c->registers[(opcode & 0x0F00) >> 8] &= c->registers[(opcode & 0x00F0) >> 4];
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x3:
			c->registers[(opcode & 0x0F00) >> 8] ^= c->registers[(opcode & 0x00F0) >> 4];
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x4:
			c->registers[(opcode & 0x0F00) >> 8] += c->registers[(opcode & 0x00F0) >> 4];
			c->registers[0xF] = c->registers[(opcode & 0x0F00) >> 8] >= c->registers[(opcode & 0x00F0) >> 4] ? 0 : 1;
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x5:
			org_value = c->registers[(opcode & 0x0F00) >> 8];
			c->registers[(opcode & 0x0F00) >> 8] -= c->registers[(opcode & 0x00F0) >> 4];
			c->registers[0xF] = c->registers[(opcode & 0x0F00) >> 8] <= org_value ? 1 : 0;
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x6:
			c->registers[0xF] = c->registers[(opcode & 0x0F00) >> 8] & 0x1;
			c->registers[(opcode & 0x0F00) >> 8] >>= 1;
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x7:
			c->registers[(opcode & 0x0F00) >> 8] = c->registers[(opcode & 0x00F0) >> 4] - c->registers[(opcode & 0x0F00) >> 8];
			c->registers[0xF] = c->registers[(opcode & 0x0F00) >> 8] <= c->registers[(opcode & 0x00F0) >> 4] ? 1 : 0;
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0xE:
			c->registers[0xF] = c->registers[(opcode & 0x0F00) >> 8] >> 7;
			c->registers[(opcode & 0x0F00) >> 8] <<= 1;
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		default:
			handle_unknown_opcode(opcode);
		}
		break;
	case 0x9000:
		if(c->registers[(opcode & 0x0F00) >> 8] != c->registers[(opcode & 0x00F0) >> 4]) {
			c->pc += BYTES_PER_INSTRUCTION;
		}
		c->pc += BYTES_PER_INSTRUCTION;
		break;
	case 0xA000:
		c->address_register = opcode & 0x0FFF;
		c->pc += BYTES_PER_INSTRUCTION;
		break;
	case 0xB000:
		c->pc = (opcode & 0x0FFF) + c->registers[0];
		break;
	case 0xC000:
		c->registers[(opcode & 0x0F00) >> 8] = (opcode & 0x00FF) & (rand() % 0xFF);
		c->pc += BYTES_PER_INSTRUCTION;
		break;
	case 0xD000:
		draw_sprite(c, c->registers[(opcode & 0x0F00) >> 8], 
			c->registers[(opcode & 0x00F0) >> 4], opcode & 0xF);
		c->pc += BYTES_PER_INSTRUCTION;
		break;
	case 0xE000:
		switch (opcode & 0x00FF) {
		case 0x9E:
			if(c->keys[c->registers[(opcode & 0x0F00) >> 8]]) {
				c->pc += BYTES_PER_INSTRUCTION;	
			}
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0xA1:
			if(!c->keys[c->registers[(opcode & 0x0F00) >> 8]]) {
				c->pc += BYTES_PER_INSTRUCTION;	
			}
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		default:
			handle_unknown_opcode(opcode);
		}
		break;
	case 0xF000:
		switch(opcode & 0x00FF) {
		case 0x07:
			c->registers[(opcode & 0x0F00) >> 8] = c->delay_timer;
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x0A:
			key_pressed = false;
			while(key_pressed == false) {
				for(i = 0; i < NR_KEYS; i++) {
					if(c->keys[i] == true) {
						c->registers[(opcode & 0x0F00) >> 8] = i;
						key_pressed = true;
						break;
					}
				}
			}
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x15:
			c->delay_timer = c->registers[(opcode & 0x0F00) >> 8];
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x18:
			c->sound_timer = c->registers[(opcode & 0x0F00) >> 8];
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x1E:
			c->address_register += c->registers[(opcode & 0x0F00) >> 8];
			c->registers[0xF] = c->address_register < c->registers[(opcode & 0x0F00) >> 8] ? 1 : 0;
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x29:
			c->address_register = c->registers[(opcode & 0x0F00) >> 8] * 5;
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x33:
			c->memory[c->address_register] = c->registers[(opcode & 0x0F00) >> 8] / 100;
			c->memory[c->address_register + 1] = (c->registers[(opcode & 0x0F00) >> 8] / 10) % 10;
			c->memory[c->address_register + 2] = c->registers[(opcode & 0x0F00) >> 8] % 10;
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x55:
			for(i = 0; i <= ((opcode & 0x0F00u) >> 8); i++) {
				c->memory[c->address_register + i] = c->registers[i];
			}
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		case 0x65:
			for(i = 0; i <= ((opcode & 0x0F00u) >> 8); i++) {
				c->registers[i] = c->memory[c->address_register + i];
			}
			c->pc += BYTES_PER_INSTRUCTION;
			break;
		default:
			handle_unknown_opcode(opcode);
		}
		break;
	default:
		handle_unknown_opcode(opcode);
	}
}

void handle_unknown_opcode(uint16_t opcode)
{
	fprintf(stderr, "Unknown opcode encoutered %u, aborting program\n", opcode);
	exit(1);
}

void clear_screen(struct chip8 *c) 
{
	for(uint32_t i = 0; i < SCREEN_SIZE; i++) {
		c->screen[i] = false;
	}
}

void return_from_subroutine(struct chip8 *c) 
{
	c->pc = c->stack[--c->stack_index];
}


void jump(struct chip8 *c, uint16_t address)
{
	c->pc = address;
}

void call_subroutine(struct chip8 *c, uint16_t address)
{
	c->stack[c->stack_index++] = c->pc + BYTES_PER_INSTRUCTION;
	jump(c, address);
}

void draw_sprite(struct chip8 *c, uint32_t screen_x, uint32_t screen_y, uint32_t rows)
{
	c->registers[0xF] = 0;
	for(uint32_t y = 0; y < rows; y++) {
		for(uint32_t x = 0; x < 8; x++) {
			uint32_t screen_index = ((screen_y + y) * 64) + ((screen_x + x) % 64);
			bool pixel_original_value = c->screen[screen_index];
			c->screen[screen_index] ^= ((c->memory[c->address_register + y] >> (7 - x)) & 0x1);
			if(pixel_original_value == true && c->screen[screen_index] == false) {
				c->registers[0xF] = 1; 
			}
		}
	}
}

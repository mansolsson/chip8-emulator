#pragma once

#include <stdint.h>
#include "chip8.h"

void execute_opcode(struct chip8 *c, uint16_t opcode);
void handle_unknown_opcode(uint16_t opcode);
void clear_screen(struct chip8 *c);
void return_from_subroutine(struct chip8 *c);
void jump(struct chip8 *c, uint16_t address);
void call_subroutine(struct chip8 *c, uint16_t address);
void draw_sprite(struct chip8 *c, uint32_t screen_x, uint32_t screen_y, uint32_t rows);
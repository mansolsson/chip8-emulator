#include <check.h>
#include <stdlib.h>
#include <stdint.h>
#include "../src/chip8.h"
#include "../src/cpu.h"

START_TEST (opcode_00E0_clears_screen) 
{
	struct chip8 c;
	uint16_t opcode = 0x00E0;
	for(int i = 0; i < SCREEN_SIZE; i++) {
		c.screen[i] = true;
	}
	c.pc = 0;

	execute_opcode(&c, opcode);

	for(int i = 0; i < SCREEN_SIZE; i++) {
		ck_assert(c.screen[i] == false);	
	}
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
} 
END_TEST

START_TEST (opcode_00EE_sets_pc_to_address_in_stack) 
{
	struct chip8 c;
	uint16_t opcode = 0x00EE;
	uint16_t address_on_stack = 0x0123;

	c.stack_index = 1;
	c.stack[0] = address_on_stack;
	execute_opcode(&c, opcode);

	ck_assert(c.stack_index == 0);
	ck_assert(c.pc == address_on_stack);
} 
END_TEST

START_TEST (opcode_1NNN_sets_pc_to_address_NNN)
{
	struct chip8 c;
	uint16_t address = 0x0234;
	uint16_t opcode = 0x1000 + address;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == address);
}
END_TEST

START_TEST (opcode_2NNN_sets_pc_to_address_NNN)
{
	struct chip8 c;
	uint16_t current_address = 1;
	uint16_t address = 0x0234;
	uint16_t opcode = 0x2000 + address;
	c.pc = current_address;
	c.stack_index = 0;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == address);
}
END_TEST

START_TEST (opcode_2NNN_store_next_instruction_on_stack)
{
	struct chip8 c;
	uint16_t current_address = 1;
	uint16_t address = 0x0234;
	uint16_t opcode = 0x2000 + address;
	c.pc = current_address;
	c.stack_index = 0;

	execute_opcode(&c, opcode);

	ck_assert(c.stack[0] == current_address + BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_3XNN_skip_next_instruction_when_register_x_equals_NN)
{
	uint16_t register_index = 1;
	uint16_t register_value = 0x0012;
	uint16_t opcode = 0x3000 + (register_index << 8) + register_value;
	uint16_t current_address = 0x0234;
	struct chip8 c;
	c.registers[register_index] = register_value;
	c.pc = current_address;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == current_address + BYTES_PER_INSTRUCTION * 2);
}
END_TEST

START_TEST (opcode_3XNN_do_not_skip_next_instruction_when_register_x_not_equals_NN)
{
	uint16_t register_index = 1;
	uint16_t register_value = 0x0012;
	uint16_t opcode = 0x3000 + (register_index << 8) + register_value;
	uint16_t current_address = 0x0234;
	struct chip8 c;
	c.registers[register_index] = register_value + 1;
	c.pc = current_address;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == current_address + BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_4XNN_skip_next_instruction_when_register_x_not_equals_NN)
{
	uint16_t register_index = 1;
	uint16_t register_value = 0x0012;
	uint16_t opcode = 0x4000 + (register_index << 8) + register_value;
	uint16_t current_address = 0x0234;
	struct chip8 c;
	c.registers[register_index] = register_value + 1;
	c.pc = current_address;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == current_address + BYTES_PER_INSTRUCTION * 2);
}
END_TEST

START_TEST (opcode_4XNN_do_not_skip_next_instruction_when_register_x_equals_NN)
{
	uint16_t register_index = 1;
	uint16_t register_value = 0x0012;
	uint16_t opcode = 0x4000 + (register_index << 8) + register_value;
	uint16_t current_address = 0x0234;
	struct chip8 c;
	c.registers[register_index] = register_value;
	c.pc = current_address;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == current_address + BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_5XY0_skip_next_instruction_when_register_x_equals_register_y)
{
	int x = 1;
	int y = 2;
	uint8_t register_value = 1;
	uint16_t opcode = 0x5120;
	uint16_t current_address = 0x0234;
	struct chip8 c;
	c.registers[x] = register_value;
	c.registers[y] = register_value;
	c.pc = current_address;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == current_address + BYTES_PER_INSTRUCTION * 2);
}
END_TEST

START_TEST (opcode_5XY0_do_not_skip_next_instruction_when_register_x_not_equals_register_y)
{
	int x = 1;
	int y = 2;
	uint8_t register_value = 1;
	uint16_t opcode = 0x5120;
	uint16_t current_address = 0x0234;
	struct chip8 c;
	c.registers[x] = register_value;
	c.registers[y] = register_value + 1;
	c.pc = current_address;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == current_address + BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_6XNN_sets_register_x_to_NN)
{
	uint8_t value = 0x23;
	int register_index = 1;
	uint16_t opcode = 0x6123;
	struct chip8 c;
	c.pc = 0;
	c.registers[register_index] = 0;
	
	execute_opcode(&c, opcode);

	ck_assert(c.registers[register_index] == value);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_7XNN_NN_is_added_to_register_x)
{
	uint8_t value = 0x23;
	uint8_t old_value = 1;
	int register_index = 1;
	uint16_t opcode = 0x7123;
	struct chip8 c;
	c.pc = 0;
	c.registers[register_index] = old_value;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[register_index] == value + old_value);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY0_set_register_x_to_value_of_register_y)
{
	uint8_t value_x = 1, value_y = 2;
	int x = 1, y = 2;
	struct chip8 c;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.pc = 0;
	uint16_t opcode = 0x8120;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == value_y);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY1_set_register_x_to_register_x_or_register_y)
{
	uint8_t value_x = 1, value_y = 2;
	int x = 1, y = 2;
	struct chip8 c;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.pc = 0;
	uint16_t opcode = 0x8121;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == (value_x | value_y));
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY2_set_register_x_to_register_x_and_register_y)
{
	uint8_t value_x = 3, value_y = 2;
	int x = 1, y = 2;
	struct chip8 c;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.pc = 0;
	uint16_t opcode = 0x8122;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == (value_x & value_y));
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY3_set_register_x_to_register_x_xor_register_y)
{
	uint8_t value_x = 3, value_y = 2;
	int x = 1, y = 2;
	struct chip8 c;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.pc = 0;
	uint16_t opcode = 0x8123;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == (value_x ^ value_y));
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY4_add_register_y_to_register_x_with_carry)
{
	int x = 1, y = 2;
	uint8_t value_x = 0xFF, value_y = 0x02;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.registers[0xF] = 0;
	uint16_t opcode = 0x8124;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == 1);
	ck_assert(c.registers[0xF] == 1);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY4_add_register_y_to_register_x_without_carry)
{
	int x = 1, y = 2;
	uint8_t value_x = 0x01, value_y = 0x02;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.registers[0xF] = 1;
	uint16_t opcode = 0x8124;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == 3);
	ck_assert(c.registers[0xF] == 0);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY5_subtract_register_y_from_register_x_with_borrow)
{
	int x = 1, y = 2;
	uint8_t value_x = 0x01, value_y = 0x02;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.registers[0xF] = 1;
	uint16_t opcode = 0x8125;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == 0xFF);
	ck_assert(c.registers[0xF] == 0);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY5_subtract_register_y_from_register_x_without_borrow)
{
	int x = 1, y = 2;
	uint8_t value_x = 0x02, value_y = 0x02;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.registers[0xF] = 0;
	uint16_t opcode = 0x8125;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == 0);
	ck_assert(c.registers[0xF] == 1);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY6_shift_register_x_one_right)
{
	int x = 1;
	uint8_t value_x = 0xFF;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[0xF] = 0;
	uint16_t opcode = 0x8106;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == 127);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY6_stores_least_significant_bit_in_register_f)
{
	int x = 1;
	uint8_t value_x = 0xFF;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[0xF] = 0;
	uint16_t opcode = 0x8106;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[0xF] == 1);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY7_subtract_register_x_from_register_y_to_register_x_with_borrow)
{
	int x = 1, y = 2;
	uint8_t value_x = 2, value_y = 1;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.registers[0xF] = 1;
	uint16_t opcode = 0x8127;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == 0xFF);
	ck_assert(c.registers[0xF] == 0);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XY7_subtract_register_x_from_register_y_to_register_x_without_borrow)
{
	int x = 1, y = 2;
	uint8_t value_x = 2, value_y = 2;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[y] = value_y;
	c.registers[0xF] = 0;
	uint16_t opcode = 0x8127;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == 0);
	ck_assert(c.registers[0xF] == 1);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XYE_shift_register_x_one_left)
{
	int x = 1;
	uint8_t value_x = 0xFF;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[0xF] = 0;
	uint16_t opcode = 0x810E;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[x] == 254);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_8XYE_stores_most_significant_bit_in_register_f)
{
	int x = 1;
	uint8_t value_x = 0xFF;
	struct chip8 c;
	c.pc = 0;
	c.registers[x] = value_x;
	c.registers[0xF] = 0;
	uint16_t opcode = 0x810E;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[0xF] == 1);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_9XY0_do_not_skip_next_instruction_when_register_x_equals_register_y)
{
	int x = 1;
	int y = 2;
	uint8_t register_value = 1;
	uint16_t opcode = 0x9120;
	uint16_t current_address = 0x0234;
	struct chip8 c;
	c.registers[x] = register_value;
	c.registers[y] = register_value;
	c.pc = current_address;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == current_address + BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_9XY0_skip_next_instruction_when_register_x_not_equals_register_y)
{
	int x = 1;
	int y = 2;
	uint8_t register_value = 1;
	uint16_t opcode = 0x9120;
	uint16_t current_address = 0x0234;
	struct chip8 c;
	c.registers[x] = register_value;
	c.registers[y] = register_value + 1;
	c.pc = current_address;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == current_address + BYTES_PER_INSTRUCTION * 2);
}
END_TEST

START_TEST (opcode_ANNN_sets_address_register_to_NNN)
{
	uint16_t opcode = 0xA123;
	struct chip8 c;
	c.address_register = 0;
	c.pc = 0;

	execute_opcode(&c, opcode);

	ck_assert(c.address_register == 0x123);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_BNNN_sets_pc_to_NNN_plus_register_0)
{
	struct chip8 c;
	c.pc = 0;
	c.registers[0] = 0x10;
	uint16_t opcode = 0xB123;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == 0x133);
}
END_TEST

START_TEST (opcode_CXNN_sets_register_x_to_register_x_and_random_number)
{
	srand(0);
	uint16_t opcode = 0xC1FF;
	struct chip8 c;
	c.registers[1] = 0;
	c.pc = 0;
	uint8_t expected_random_number = 163;

	execute_opcode(&c, opcode);

	ck_assert(c.registers[1] == expected_random_number);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_DXYN_register_f_is_not_set_when_no_pixel_is_toggled_off)
{
	int i;
	struct chip8 c;
	c.address_register = 0;
	c.pc = 0;
	for(i = 0; i < SCREEN_SIZE; i++) {
		c.screen[i] = false;
	}
	c.registers[1] = 0;
	c.registers[2] = 1;
	c.registers[0xF] = 1;
	c.memory[0] = 5;
	c.memory[1] = 2;
	uint16_t opcode = 0xD122;

	execute_opcode(&c, opcode);

	ck_assert(c.screen[64 + 5] == true);
	ck_assert(c.screen[64 + 7] == true);
	ck_assert(c.screen[64 * 2 + 6] == true);
	ck_assert(c.registers[0xF] == 0);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_DXYN_register_f_is_set_when_a_pixel_is_toggled_off)
{
	int i;
	struct chip8 c;
	c.address_register = 0;
	c.pc = 0;
	for(i = 0; i < SCREEN_SIZE; i++) {
		c.screen[i] = false;
	}
	c.screen[64 + 5] = true;
	c.registers[1] = 0;
	c.registers[2] = 1;
	c.registers[0xF] = 0;
	c.memory[0] = 5;
	c.memory[1] = 2;
	uint16_t opcode = 0xD122;

	execute_opcode(&c, opcode);

	ck_assert(c.screen[64 + 5] == false);
	ck_assert(c.screen[64 + 7] == true);
	ck_assert(c.screen[64 * 2 + 6] == true);
	ck_assert(c.registers[0xF] == 1);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_EX9E_skips_next_if_key_at_register_x_is_pressed)
{
	struct chip8 c;
	c.pc = 0;
	c.registers[1] = 1;
	c.keys[1] = true;
	uint16_t opcode = 0xE19E;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == BYTES_PER_INSTRUCTION * 2);
}
END_TEST

START_TEST (opcode_EX9E_do_not_skip_next_if_key_at_register_x_is_not_pressed)
{
struct chip8 c;
	c.pc = 0;
	c.registers[1] = 1;
	c.keys[1] = false;
	uint16_t opcode = 0xE19E;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_EXA1_skips_next_if_key_at_register_x_is_not_pressed)
{
	struct chip8 c;
	c.pc = 0;
	c.registers[1] = 1;
	c.keys[1] = false;
	uint16_t opcode = 0xE1A1;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == BYTES_PER_INSTRUCTION * 2);
}
END_TEST

START_TEST (opcode_EXA1_do_not_skip_next_if_key_at_register_x_is_pressed)
{
struct chip8 c;
	c.pc = 0;
	c.registers[1] = 1;
	c.keys[1] = true;
	uint16_t opcode = 0xE1A1;

	execute_opcode(&c, opcode);

	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX07_set_register_to_delay_timer)
{
	uint8_t delay_timer = 12;
	struct chip8 c;
	c.delay_timer = delay_timer;
	c.registers[2] = 0;
	c.pc = 0;

	execute_opcode(&c, 0xF207);

	ck_assert(c.registers[2] == delay_timer);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX0A_set_register_x_to_pressed_key)
{
	int i;
	struct chip8 c;
	c.pc = 0;
	for(i = 0; i < NR_KEYS; i++) {
		c.keys[i] = false;
	}
	c.keys[5] = true;
	c.registers[1] = 0;

	execute_opcode(&c, 0xF10A);

	ck_assert(c.registers[1] == 5);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX15_set_delay_timer_to_register_x)
{
	struct chip8 c;
	c.pc = 0;
	c.registers[2] = 10;
	c.delay_timer = 0;

	execute_opcode(&c, 0xF215);

	ck_assert(c.delay_timer == 10);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX18_set_sound_timer_to_register_x)
{
	struct chip8 c;
	c.pc = 0;
	c.registers[2] = 10;
	c.sound_timer = 0;

	execute_opcode(&c, 0xF218);

	ck_assert(c.sound_timer == 10);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX1E_add_register_x_to_address_register_with_carry)
{
	struct chip8 c;
	c.pc = 0;
	c.address_register = 0xFFFF;
	c.registers[2] = 2;
	c.registers[0xF] = 0;

	execute_opcode(&c, 0xF21E);

	ck_assert(c.address_register == 1);
	ck_assert(c.registers[0xF] == 1);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX1E_add_register_x_to_address_register_without_carry)
{
	struct chip8 c;
	c.pc = 0;
	c.address_register = 0x1;
	c.registers[2] = 2;
	c.registers[0xF] = 1;

	execute_opcode(&c, 0xF21E);

	ck_assert(c.address_register == 3);
	ck_assert(c.registers[0xF] == 0);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX29_sets_address_register_to_memory_index_of_sprite_in_register_x)
{
	struct chip8 c;
	c.pc = 0;
	c.address_register = 0;
	c.registers[2] = 6;

	execute_opcode(&c, 0xF229);

	ck_assert(c.address_register = 6 * 5);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX33_store_decimal_representation_in_memory)
{
	struct chip8 c;
	c.pc = 0;
	c.address_register = 1;
	c.registers[2] = 123;

	execute_opcode(&c, 0xF233);

	ck_assert(c.memory[c.address_register] == 1);
	ck_assert(c.memory[c.address_register + 1] == 2);
	ck_assert(c.memory[c.address_register + 2] == 3);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX55_store_registers_in_memory)
{
	struct chip8 c;
	c.pc = 0;
	c.registers[0] = 10;
	c.registers[1] = 8;
	c.registers[2] = 56;
	c.registers[3] = 3;
	c.address_register = 5;
	c.memory[5 + 3] = 0;

	execute_opcode(&c, 0xF255);

	ck_assert(c.memory[c.address_register] == 10);
	ck_assert(c.memory[c.address_register + 1] == 8);
	ck_assert(c.memory[c.address_register + 2] == 56);
	ck_assert(c.memory[c.address_register + 3] == 0);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

START_TEST (opcode_FX65_store_memory_in_registers)
{
	struct chip8 c;
	c.pc = 0;
	c.registers[3] = 3;
	c.address_register = 5;
	c.memory[5] = 11;
	c.memory[5 + 1] = 31;
	c.memory[5 + 2] = 2;
	c.memory[5 + 3] = 0;

	execute_opcode(&c, 0xF265);

	ck_assert(c.registers[0] == 11);
	ck_assert(c.registers[1] == 31);
	ck_assert(c.registers[2] == 2);
	ck_assert(c.registers[3] == 3);
	ck_assert(c.pc == BYTES_PER_INSTRUCTION);
}
END_TEST

Suite * opcode_suite()
{
    Suite *suite;
    TCase *tc_core;

    suite = suite_create("Opcode");
    tc_core = tcase_create("Core");

    tcase_add_test(tc_core, opcode_00E0_clears_screen);
	tcase_add_test(tc_core, opcode_00EE_sets_pc_to_address_in_stack);
	tcase_add_test(tc_core, opcode_1NNN_sets_pc_to_address_NNN);
	tcase_add_test(tc_core, opcode_2NNN_sets_pc_to_address_NNN);
	tcase_add_test(tc_core, opcode_2NNN_store_next_instruction_on_stack);
	tcase_add_test(tc_core, opcode_3XNN_skip_next_instruction_when_register_x_equals_NN);
	tcase_add_test(tc_core, opcode_3XNN_do_not_skip_next_instruction_when_register_x_not_equals_NN);
	tcase_add_test(tc_core, opcode_4XNN_skip_next_instruction_when_register_x_not_equals_NN);
	tcase_add_test(tc_core, opcode_4XNN_do_not_skip_next_instruction_when_register_x_equals_NN);
	tcase_add_test(tc_core, opcode_5XY0_skip_next_instruction_when_register_x_equals_register_y);
	tcase_add_test(tc_core, opcode_5XY0_do_not_skip_next_instruction_when_register_x_not_equals_register_y);
	tcase_add_test(tc_core, opcode_6XNN_sets_register_x_to_NN);
	tcase_add_test(tc_core, opcode_7XNN_NN_is_added_to_register_x);
	tcase_add_test(tc_core, opcode_8XY0_set_register_x_to_value_of_register_y);
	tcase_add_test(tc_core, opcode_8XY1_set_register_x_to_register_x_or_register_y);
	tcase_add_test(tc_core, opcode_8XY2_set_register_x_to_register_x_and_register_y);
	tcase_add_test(tc_core, opcode_8XY3_set_register_x_to_register_x_xor_register_y);
	tcase_add_test(tc_core, opcode_8XY4_add_register_y_to_register_x_with_carry);
	tcase_add_test(tc_core, opcode_8XY4_add_register_y_to_register_x_without_carry);
	tcase_add_test(tc_core, opcode_8XY5_subtract_register_y_from_register_x_with_borrow);
	tcase_add_test(tc_core, opcode_8XY5_subtract_register_y_from_register_x_without_borrow);
	tcase_add_test(tc_core, opcode_8XY6_shift_register_x_one_right);
	tcase_add_test(tc_core, opcode_8XY6_stores_least_significant_bit_in_register_f);
	tcase_add_test(tc_core, opcode_8XY7_subtract_register_x_from_register_y_to_register_x_with_borrow);
	tcase_add_test(tc_core, opcode_8XY7_subtract_register_x_from_register_y_to_register_x_without_borrow);
	tcase_add_test(tc_core, opcode_8XYE_shift_register_x_one_left);
	tcase_add_test(tc_core, opcode_8XYE_stores_most_significant_bit_in_register_f);
	tcase_add_test(tc_core, opcode_9XY0_skip_next_instruction_when_register_x_not_equals_register_y);
	tcase_add_test(tc_core, opcode_9XY0_do_not_skip_next_instruction_when_register_x_equals_register_y);
	tcase_add_test(tc_core, opcode_ANNN_sets_address_register_to_NNN);
	tcase_add_test(tc_core, opcode_BNNN_sets_pc_to_NNN_plus_register_0);
	tcase_add_test(tc_core, opcode_CXNN_sets_register_x_to_register_x_and_random_number);
	tcase_add_test(tc_core, opcode_DXYN_register_f_is_set_when_a_pixel_is_toggled_off);
	tcase_add_test(tc_core, opcode_DXYN_register_f_is_not_set_when_no_pixel_is_toggled_off);
	tcase_add_test(tc_core, opcode_EX9E_skips_next_if_key_at_register_x_is_pressed);
	tcase_add_test(tc_core, opcode_EX9E_do_not_skip_next_if_key_at_register_x_is_not_pressed);
	tcase_add_test(tc_core, opcode_EXA1_skips_next_if_key_at_register_x_is_not_pressed);
	tcase_add_test(tc_core, opcode_EXA1_do_not_skip_next_if_key_at_register_x_is_pressed);
	tcase_add_test(tc_core, opcode_FX07_set_register_to_delay_timer);
	tcase_add_test(tc_core, opcode_FX0A_set_register_x_to_pressed_key);
	tcase_add_test(tc_core, opcode_FX15_set_delay_timer_to_register_x);
	tcase_add_test(tc_core, opcode_FX18_set_sound_timer_to_register_x);
	tcase_add_test(tc_core, opcode_FX1E_add_register_x_to_address_register_with_carry);
	tcase_add_test(tc_core, opcode_FX1E_add_register_x_to_address_register_without_carry);
	tcase_add_test(tc_core, opcode_FX29_sets_address_register_to_memory_index_of_sprite_in_register_x);
	tcase_add_test(tc_core, opcode_FX33_store_decimal_representation_in_memory);
	tcase_add_test(tc_core, opcode_FX55_store_registers_in_memory);
	tcase_add_test(tc_core, opcode_FX65_store_memory_in_registers);

    suite_add_tcase(suite, tc_core);

    return suite;
}

int main()
{
    int number_failed;
    Suite *s;
    SRunner *sr;

    s = opcode_suite();
    sr = srunner_create(s);

    srunner_run_all(sr, CK_NORMAL);
    number_failed = srunner_ntests_failed(sr);
    srunner_free(sr);
    return (number_failed == 0) ? EXIT_SUCCESS : EXIT_FAILURE;
}

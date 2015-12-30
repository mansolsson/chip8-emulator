#!/bin/sh
set -e;

prepare() {
	if [ -d "build" ]; then
		rm -r build;
	fi
	mkdir -p build/test;
}

run_opcode_test() {
	gcc src/chip8.c test/chip8_test.c -lcheck -lm -lpthread -lrt -o build/test/chip8;
	build/test/chip8;
}

main() {
	prepare;
	run_opcode_test;
}
main;

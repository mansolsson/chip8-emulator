#!/bin/sh
set -e;

prepare() {
	if [ -d "build" ]; then
		rm -r build;
	fi
	mkdir -p build/test;
}

run_opcode_test() {
	gcc src/chip8.c test/chip8_test.c -std=gnu99 -lcheck -lm -lpthread -lrt -o build/test/chip8;
	build/test/chip8;
}

compile() {
	gcc src/chip8.c src/graphics.c src/emulator.c src/main.c -std=gnu99 -lSDL2 -lm -o build/chip8;
}

main() {
	prepare;
	run_opcode_test;
	compile;
}
main;

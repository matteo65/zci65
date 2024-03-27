#include <stdio.h>
#include <stdint.h> // uint8_t

#include "zci65.h"

int main(void)
{
	int8_t key[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,
				    18,19,20,21,22,23,24,25,26,27,28,29,30,31};
				   
	// Array to encipher
	int8_t buffer[] = {'H','e','l','l','o',',',' ','w','o','r','l','d','!'};

	// Print the original input
	for(int i = 0; i < 13; i++)
		printf("%c", buffer[i]);

	printf("\n");

	// Init the encipher/decipher
	z65_init(key);
	
	// Encipher the array
	z65_encipherArray(buffer, 13);
	
	// Print the encipher output
	for(int i = 0; i < 13; i++)
		printf("%c", buffer[i]);
	
	printf("\n");
	
	// Reset (in order to switch in decipher mode)
	z65_reset();
	
	// Decipher the array
	z65_decipherArray(buffer, 13);
	
	// Print the decipher output (same as the original)
	for(int i = 0; i < 13; i++)
		printf("%c", buffer[i]);

	printf("\n");	
}

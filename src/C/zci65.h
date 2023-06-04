#ifndef _ZCI65_H
#define _ZCI65_H

#include <stdint.h> // uint32_t

#ifdef __cplusplus
extern "C" {
#endif

/**
 * Uses key[] to generate the initial random values of s[], k and c 
 */
void z65_reset(void);

/**
 * Initializatot of the encipher/decipher
 * You cannot mix calls to encryption methods with decryption 
 * methods: you can do this by calling the reset() method which 
 * resets the state to the initial conditions
 * @param k non-null: 32 byte length secret key
 */
void z65_init(int8_t k[]);

/**
 * Encipher a byte
 * @param b: byte to be enciphered
 * @return the enciphered byte
 */
int8_t z65_encipher(int8_t b);

/**
 * Encipher the byte less significant of a int value
 * @param b: int to encipher
 * @return the input value with the byte less significant enciphered
 */
int z65_encipherInt(int b);

/**
 * Encipher an array of byte and replace every byte with the enciphered value
 * @param data: the array to encipher
 * @param len: the number of byte to be enciphered
 */
void z65_encipherArray(int8_t data[], int32_t len);

/**
 * Encipher an array of byte and write the enciphered output into an other array 
 * @param data: the array to encipher
 * @param len: the number of byte to be enciphered
 * @param output: array length at least len bytes
 */
void z65_encipherOut(const int8_t data[], int32_t len, int8_t output[]);

/**
 * Decipher a byte
 * @param b: byte to be decipher
 * @return the deciphered byte
 */
int8_t z65_decipher(int8_t b);

/**
 * Decipher the byte less significant of a int value
 * @param b: int to decipher
 * @return the input value with the byte less significant deciphered
 */
int z65_decipherInt(int b);

/**
 * Decipher an array of byte and replace every byte with the enciphered value
 * @param data: the array to decipher
 * @param len: the number of byte to be enciphered
 */
void z65_decipherArray(int8_t data[], int32_t len);

/**
 * Decipher an array of byte and write the deciphered output into an other array 
 * @param data: the array to decipher
 * @param len: the number of byte to be deciphered
 * @param output: array length at least len bytes
 */
void z65_decipherOut(const int8_t data[], int32_t len, int8_t output[]);

#ifdef __cplusplus
}
#endif

#endif
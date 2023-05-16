#ifndef _MZHASH64_H
#define _MZHASH64_H

#include <stdint.h> // uint64_t

#ifdef __cplusplus
extern "C" {
#endif

#define MZ_HASH_64_DEFAULT_SEED		0;

uint64_t mzhash64(const int8_t *data, int length, uint64_t seed);

#ifdef __cplusplus
}
#endif

#endif
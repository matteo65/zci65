# zci65
Fast, secure, efficent stream cipher

Zci65 uses a 32-byte key and does not use a Initialization Vector (IV).
The algorithm use a internal 256 table of 32 bit integer and other 2 internal 32 bit fields; in total the state size is 1088 bytes.
The internal state is continuously changed by the source content to be encrypted

// src/utils/useHashids.js

import Hashids from "hashids";

export function useHashids(
  salt = import.meta.env.VITE_HASHIDS_SALT,
  minLength = 6
) {
  const hashids = new Hashids(salt, minLength);
  const encode = (number) => {
    if (typeof number !== "number" || isNaN(number)) {
      throw new Error("Giá trị truyền vào phải là số");
    }
    return hashids.encode(number);
  };

  const decode = (hash) => {
    const decoded = hashids.decode(hash);
    return decoded.length > 0 ? decoded[0] : null;
  };

  return {
    encode,
    decode,
  };
}

package com.joey.io;

import com.joey.databasemanager.protocol.jap.DecoderException;

public class LSBStrategy extends Strategy {
	
	private BitBuffer container;
	
	LSBStrategy(BitBuffer cont) {
		this.container = cont;
	}

	@Override
	public void addInteger(int val, int numberofbits) {
		int targetindex = 0;
		int processedbits = 0;
		//1.usedbits is 0
		//1.size is 8
		//2.usedBits is 6
		//2.size is 8
		for(int i = 1;i<=container.size/8;i++) {
			if ( container.usedBits < (i*8)) {
				targetindex = i-1;
				break;
			}
		}
		//1.targetindex is 0
		//1.8-0 is 8 is not less than 6(numberofbits)
		//2.targetindex is 0
		//2. 2 is less than 4 so increase size
		if((container.size-container.usedBits)<numberofbits) {
			for(int i=1;i<10;i++) {
				/**
				 * Increase the size of the container
				 */
				container.increase(1);
				//2. 16-6 is 10>4 we can break now
				if ((container.size-container.usedBits)>=numberofbits) {
					break;
				}
			}
		}
		for(int i = 1;i<=container.size/8;i++) {
			if ( container.usedBits < (i*8)) {
				targetindex = i-1;
				break;
			}
		}
		int possiblebits = 0;
		if(container.size-container.usedBits <= 8 ) {
			possiblebits = container.size - container.usedBits;
		}
		else {
			//2. 10-8*(2-1) is 2 bits can be set in current position target
			possiblebits = (container.size - container.usedBits) - 8*((container.size/8)-1);
		}
		//possiblebits is 8
		/**
		 * Now we can are supposed to set
		 * the targetindex and the length of bits that can be set are possiblebits
		 */
		//2. 2 is not > 4 so we cant satisfy this condition
		if (possiblebits >= numberofbits) {
			//do we have to mask to avoid using more bits than allowed?
			container.value[targetindex] = (byte) ((val<<(8-possiblebits)) | container.value[targetindex]);
			container.usedBits = container.usedBits+numberofbits;
			//1.usedBits is 0+6 = 6
			return;
		}
		else {
			/**
			 * we have more bits to set than the
			 * bits available to be set in the
			 * current position
			 */
			//2. shift by number of used bits
			container.value[targetindex] = (byte) ((val<<(8-possiblebits)) | container.value[targetindex]);
			processedbits = possiblebits;
			container.usedBits = container.usedBits + processedbits;
			//2. processbits is 2
			while(processedbits<numberofbits) {
				targetindex++;
				possiblebits = 8;
				int bitsleft = numberofbits-processedbits;
				if(bitsleft <= 8) { 
					container.value[targetindex] = (byte) ((val>>processedbits)| container.value[targetindex]);
					processedbits = processedbits + bitsleft;
					container.usedBits = container.usedBits + bitsleft;
					return;
				}
				else {
					container.value[targetindex] = (byte) ((val>>processedbits)|container.value[targetindex]);
					processedbits = processedbits + 8;
					container.usedBits = container.usedBits + 8;
					continue;
				}
				
			}
		}
	}

	@Override
	public int getInteger(int numberofbits) throws DecoderException {
		if(container.size<numberofbits) {
			throw new DecoderException("not enough bits present in container");
		}
		int targetindex = 0;
		for(int i=1;i<=container.size/8;i++) {
			if(container.readBits<=i*8) {
				targetindex = i-1;
				break;
			}
		}
		int possiblebits = 0;
		if(container.size-container.readBits <= 8 ) {
			possiblebits = container.size - container.readBits;
		}
		else {
			possiblebits = (container.size - container.readBits) - 8*((container.size/8)-1);
		}
		
		if (possiblebits >= numberofbits) {
			int ret = (container.value[targetindex] >> (8-possiblebits)) & 0xFF;
			container.readBits = container.readBits + numberofbits;
			if (numberofbits<=8) {
				ret = ret & container.mask.get(numberofbits);
				return ret;
			}
		}
		else {
			int ret = (container.value[targetindex] >> (8-possiblebits)) & container.mask.get(possiblebits);
			container.readBits = container.readBits + possiblebits;
			int processedbits = possiblebits;
			int nextindexval;
			while(processedbits<numberofbits) {
				targetindex++;
				int bitsleft = numberofbits - processedbits;
				nextindexval = container.value[targetindex];
				
				if (bitsleft <=8) {
					container.readBits = container.readBits + bitsleft;
					nextindexval = (byte)nextindexval & container.mask.get(bitsleft).byteValue();
					nextindexval = (nextindexval<<processedbits) | ret;
					processedbits = processedbits + bitsleft;
					return nextindexval;
				}
				else {
					container.readBits = container.readBits + 8;
					nextindexval = (nextindexval<<processedbits) | ret;
					processedbits = processedbits + 8;
					ret = nextindexval;
					continue;
				}
			}
			
		}
		
		throw new DecoderException("unable to decode");
	}
	
}
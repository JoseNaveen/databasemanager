package com.joey.io;

import com.joey.databasemanager.protocol.jap.DecoderException;

public class MSBStrategy extends Strategy {
	
	private BitBuffer container;
	
	MSBStrategy(BitBuffer cont) {
		this.container = cont;
	}
	@Override
	public void addInteger(final int val, int numberofbits) {
		int targetindex = 0;
		int processedbits = 0;
		for(int i = 1;i<=container.size/8;i++) {
			if ( container.usedBits < (i*8)) {
				targetindex = i-1;
				break;
			}
		}
		if((container.size-container.usedBits)<numberofbits) {
			for(int i=1;i<10;i++) {
				/**
				 * Increase the size of the container
				 */
				container.increase(1);
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
			possiblebits = 8 - (container.usedBits % 8);
		}
		/**
		 * Now we are supposed to set
		 * the targetindex and the length of bits that can be set are possiblebits
		 */
		if (possiblebits >= numberofbits) {
			//do we have to mask to avoid using more bits than allowed?
			container.value[targetindex] = (byte) ((val<<(8-numberofbits-(container.usedBits % 8))) | container.value[targetindex]);
			container.usedBits = container.usedBits+numberofbits;
			return;
		}
		else {
			/**
			 * we have more bits to set than the
			 * bits available to be set in the
			 * current position
			 */
			container.value[targetindex] = (byte) ((val>>(8-possiblebits+(numberofbits - 8))) | container.value[targetindex]);
			processedbits = possiblebits;
			container.usedBits = container.usedBits + processedbits;
			while(processedbits<numberofbits) {
				targetindex++;
				possiblebits = 8;
				int bitsleft = numberofbits-processedbits;
				if(bitsleft <= 8) { 
					container.value[targetindex] = (byte) ((val<<processedbits-numberofbits+8)| container.value[targetindex]);
					processedbits = processedbits + bitsleft;
					container.usedBits = container.usedBits + bitsleft;
					return;
				}
				else {
					int tmp = val<<processedbits;
					container.value[targetindex] = (byte) ((tmp>>(numberofbits-8))|container.value[targetindex]);
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
			if(container.readBits<i*8) {
				targetindex = i-1;
				break;
			}
		}
		int possiblebits = 0;
		if(container.size-container.readBits <= 8 ) {
			possiblebits = container.size - container.readBits;
		}
		else {
			possiblebits = 8 - (container.readBits % 8);
		}
		
		if (possiblebits >= numberofbits) {
			int bitsread = container.readBits % 8;
			int tmpvalue = container.value[targetindex] << bitsread;
			int ret = (tmpvalue >> (8-numberofbits)) & BitBuffer.mask.get(numberofbits);
			container.readBits = container.readBits + numberofbits;
			return ret;
		}
		else {
			int tmpvalue = container.value[targetindex] & BitBuffer.mask.get(possiblebits);
			int ret = tmpvalue << (numberofbits - possiblebits);
			container.readBits = container.readBits + possiblebits;
			int processedbits = possiblebits;
			int nextindexval;
			while(processedbits<numberofbits) {
				targetindex++;
				int bitsleft = numberofbits - processedbits;
				nextindexval = container.value[targetindex];
				
				if (bitsleft <=8) {
					container.readBits = container.readBits + bitsleft;
					nextindexval = ((byte)nextindexval >> 8-bitsleft) & BitBuffer.mask.get(bitsleft);
					nextindexval = nextindexval | ret;
					processedbits = processedbits + bitsleft;
					return nextindexval;
				}
				else {
					container.readBits = container.readBits + 8;
					nextindexval = (nextindexval<<(bitsleft-8)) | ret;
					processedbits = processedbits + 8;
					ret = nextindexval;
					continue;
				}
			}
			
		}
		
		throw new DecoderException("unable to decode");
	}
	
}
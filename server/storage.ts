// Storage interface - placeholder for compatibility
import { DevoteeType, NamhattaType } from '../shared/schema.js';

export interface IStorage {
  // Devotee operations
  getDevotees(): Promise<DevoteeType[]>;
  getDevotee(id: number): Promise<DevoteeType | null>;
  
  // Namhatta operations  
  getNamhattas(): Promise<NamhattaType[]>;
  getNamhatta(id: number): Promise<NamhattaType | null>;
}

// In-memory storage implementation
export class MemStorage implements IStorage {
  private devotees: DevoteeType[] = [];
  private namhattas: NamhattaType[] = [];

  async getDevotees(): Promise<DevoteeType[]> {
    return this.devotees;
  }

  async getDevotee(id: number): Promise<DevoteeType | null> {
    return this.devotees.find(d => d.id === id) || null;
  }

  async getNamhattas(): Promise<NamhattaType[]> {
    return this.namhattas;
  }

  async getNamhatta(id: number): Promise<NamhattaType | null> {
    return this.namhattas.find(n => n.id === id) || null;
  }
}

export const storage = new MemStorage();
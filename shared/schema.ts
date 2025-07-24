// Database schema types and validation schemas
export interface DevoteeType {
  id: number;
  name: string;
  email?: string;
  phone?: string;
  status: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface NamhattaType {
  id: number;
  name: string;
  secretary: string;
  status: string;
  createdAt: Date;
  updatedAt: Date;
}

// Basic types to support the application structure
export type InsertDevotee = Omit<DevoteeType, 'id' | 'createdAt' | 'updatedAt'>;
export type InsertNamhatta = Omit<NamhattaType, 'id' | 'createdAt' | 'updatedAt'>;
// Types for Spring Boot backend communication
import { z } from 'zod';

// Core entity interfaces (matching Spring Boot DTOs)
export interface Devotee {
  id: number;
  name: string;
  email?: string;
  phone?: string;
  statusId?: number;
  courses?: any[];
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface Namhatta {
  id: number;
  name: string;
  secretary: string;
  status: string;
  shraddhakutirId?: number;
  establishedDate?: string;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface DevotionalStatus {
  id: number;
  name: string;
  description?: string;
  hierarchy: number;
  createdAt: string;
  updatedAt: string;
}

export interface Shraddhakutir {
  id: number;
  name: string;
  description?: string;
  status: string;
  createdAt: string;
  updatedAt: string;
}

export interface Leader {
  id: number;
  name: string;
  title: string;
  email?: string;
  phone?: string;
  hierarchy: number;
  createdAt: string;
  updatedAt: string;
}

export interface Address {
  id: number;
  country?: string;
  state?: string;
  district?: string;
  subDistrict?: string;
  village?: string;
  postalCode?: string;
  createdAt: string;
  updatedAt: string;
}

export interface User {
  id: number;
  username: string;
  role: string;
  active: boolean;
  lastLogin?: string;
  createdAt: string;
  updatedAt: string;
}

export interface DevoteeAddress {
  id: number;
  devoteeId: number;
  addressId: number;
  landmark?: string;
  addressType: string;
  createdAt: string;
}

export interface NamhattaAddress {
  id: number;
  namhattaId: number;
  addressId: number;
  landmark?: string;
  addressType: string;
  createdAt: string;
}

export interface UserDistrict {
  id: number;
  userId: number;
  districtCode: string;
  districtName: string;
  createdAt: string;
}

export interface UserSession {
  id: number;
  userId: number;
  sessionToken: string;
  expiresAt: string;
  createdAt: string;
}

export interface JwtBlacklistEntry {
  id: number;
  token: string;
  expiresAt: string;
  createdAt: string;
}

// Input types for creating entities
export type InsertDevotee = Omit<Devotee, 'id' | 'createdAt' | 'updatedAt'>;
export type InsertNamhatta = Omit<Namhatta, 'id' | 'createdAt' | 'updatedAt'>;
export type InsertDevotionalStatus = Omit<DevotionalStatus, 'id' | 'createdAt' | 'updatedAt'>;
export type InsertShraddhakutir = Omit<Shraddhakutir, 'id' | 'createdAt' | 'updatedAt'>;
export type InsertLeader = Omit<Leader, 'id' | 'createdAt' | 'updatedAt'>;
export type InsertAddress = Omit<Address, 'id' | 'createdAt' | 'updatedAt'>;
export type InsertUser = Omit<User, 'id' | 'createdAt' | 'updatedAt' | 'lastLogin'>;

// Additional validation schemas for complex operations
export const loginSchema = z.object({
  username: z.string().min(1, 'Username is required'),
  password: z.string().min(1, 'Password is required'),
});

export const devoteeWithAddressSchema = z.object({
  name: z.string().min(1, 'Name is required'),
  email: z.string().email().optional().or(z.literal('')),
  phone: z.string().optional(),
  statusId: z.number().optional(),
  courses: z.array(z.any()).optional(),
  notes: z.string().optional(),
  country: z.string().optional(),
  state: z.string().optional(),
  district: z.string().optional(),
  subDistrict: z.string().optional(),
  village: z.string().optional(),
  postalCode: z.string().optional(),
  landmark: z.string().optional(),
});

export const namhattaWithAddressSchema = z.object({
  name: z.string().min(1, 'Name is required'),
  secretary: z.string().min(1, 'Secretary is required'),
  status: z.string().default('active'),
  shraddhakutirId: z.number().optional(),
  establishedDate: z.string().optional(),
  notes: z.string().optional(),
  country: z.string().optional(),
  state: z.string().optional(),
  district: z.string().optional(),
  subDistrict: z.string().optional(),
  village: z.string().optional(),
  postalCode: z.string().optional(),
  landmark: z.string().optional(),
});

export type LoginRequest = z.infer<typeof loginSchema>;
export type DevoteeWithAddress = z.infer<typeof devoteeWithAddressSchema>;
export type NamhattaWithAddress = z.infer<typeof namhattaWithAddressSchema>;
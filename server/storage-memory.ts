import { Devotee, InsertDevotee, Namhatta, InsertNamhatta, DevotionalStatus, InsertDevotionalStatus, Shraddhakutir, InsertShraddhakutir, NamhattaUpdate, InsertNamhattaUpdate, Leader, InsertLeader, StatusHistory } from "@shared/schema";
import { IStorage } from "./storage-fresh";

export class MemoryStorage implements IStorage {
  private devotees: Devotee[] = [];
  private namhattas: Namhatta[] = [];
  private devotionalStatuses: DevotionalStatus[] = [];
  private shraddhakutirs: Shraddhakutir[] = [];
  private namhattaUpdates: NamhattaUpdate[] = [];
  private leaders: Leader[] = [];
  private statusHistory: StatusHistory[] = [];

  private nextId = 1;

  constructor() {
    this.initializeData();
  }

  private initializeData() {
    // Initialize devotional statuses
    this.devotionalStatuses = [
      { id: 1, name: "Shraddhavan", createdAt: new Date() },
      { id: 2, name: "Sadhusangi", createdAt: new Date() },
      { id: 3, name: "Gour/Krishna Sevak", createdAt: new Date() },
      { id: 4, name: "Gour/Krishna Sadhak", createdAt: new Date() },
      { id: 5, name: "Sri Guru Charan Asraya", createdAt: new Date() },
      { id: 6, name: "Harinam Diksha", createdAt: new Date() },
      { id: 7, name: "Pancharatrik Diksha", createdAt: new Date() }
    ];

    // Initialize leaders
    this.leaders = [
      { id: 1, name: "His Divine Grace A.C. Bhaktivedanta Swami Prabhupada", role: "FOUNDER_ACHARYA", reportingTo: null, location: { country: "India" }, createdAt: new Date() },
      { id: 2, name: "His Holiness Jayapataka Swami", role: "GBC", reportingTo: 1, location: { country: "India" }, createdAt: new Date() },
      { id: 3, name: "HH Gauranga Prem Swami", role: "REGIONAL_DIRECTOR", reportingTo: 2, location: { country: "India", state: "West Bengal" }, createdAt: new Date() },
      { id: 4, name: "HH Bhaktivilasa Gaurachandra Swami", role: "CO_REGIONAL_DIRECTOR", reportingTo: 3, location: { country: "India", state: "West Bengal" }, createdAt: new Date() },
      { id: 5, name: "HG Padmanetra Das", role: "CO_REGIONAL_DIRECTOR", reportingTo: 3, location: { country: "India", state: "West Bengal" }, createdAt: new Date() },
      { id: 6, name: "District Supervisor - Nadia", role: "DISTRICT_SUPERVISOR", reportingTo: 4, location: { country: "India", state: "West Bengal", district: "Nadia" }, createdAt: new Date() },
      { id: 7, name: "Mala Senapoti - Mayapur", role: "MALA_SENAPOTI", reportingTo: 6, location: { country: "India", state: "West Bengal", district: "Nadia" }, createdAt: new Date() }
    ];

    // Initialize shraddhakutirs
    this.shraddhakutirs = [
      { id: 1, name: "Mayapur Shraddhakutir", districtCode: "NADIA", createdAt: new Date() },
      { id: 2, name: "Kolkata Shraddhakutir", districtCode: "KOLKATA", createdAt: new Date() },
      { id: 3, name: "Bhubaneswar Shraddhakutir", districtCode: "KHORDHA", createdAt: new Date() },
      { id: 4, name: "Patna Shraddhakutir", districtCode: "PATNA", createdAt: new Date() },
      { id: 5, name: "Ranchi Shraddhakutir", districtCode: "RANCHI", createdAt: new Date() }
    ];

    // Initialize sample namhattas
    this.namhattas = [
      { id: 1, code: "MAYAPUR001", name: "Mayapur Central Namhatta", meetingDay: "Sunday", meetingTime: "09:00", malaSenapoti: "Gauranga Das", mahaChakraSenapoti: "Krishna Das", chakraSenapoti: "Radha Das", upaChakraSenapoti: "Gopal Das", secretary: "Nitai Das", status: "APPROVED", createdAt: new Date(), updatedAt: new Date() },
      { id: 2, code: "KOLKATA001", name: "Kolkata Central Namhatta", meetingDay: "Saturday", meetingTime: "18:00", malaSenapoti: "Hare Krishna Das", mahaChakraSenapoti: "Bhakti Das", chakraSenapoti: "Prema Das", upaChakraSenapoti: "Seva Das", secretary: "Govinda Das", status: "APPROVED", createdAt: new Date(), updatedAt: new Date() }
    ];

    // Initialize sample devotees
    this.devotees = [
      { id: 1, legalName: "Ramesh Kumar", name: "Gauranga Das", dob: "1985-05-15", email: "ramesh@example.com", phone: "9876543210", fatherName: "Suresh Kumar", motherName: "Sita Devi", gender: "MALE", bloodGroup: "O+", maritalStatus: "MARRIED", devotionalStatusId: 6, namhattaId: 1, gurudevHarinam: 2, gurudevPancharatrik: 2, initiatedName: "Gauranga Das", harinamDate: "2020-01-15", education: "B.Tech", occupation: "Software Engineer", devotionalCourses: [], shraddhakutirId: 1, createdAt: new Date(), updatedAt: new Date() },
      { id: 2, legalName: "Priya Sharma", name: "Radha Dasi", dob: "1990-08-22", email: "priya@example.com", phone: "9876543211", fatherName: "Raj Sharma", motherName: "Uma Sharma", gender: "FEMALE", bloodGroup: "A+", maritalStatus: "UNMARRIED", devotionalStatusId: 3, namhattaId: 1, gurudevHarinam: null, gurudevPancharatrik: null, initiatedName: "Radha Dasi", education: "M.A.", occupation: "Teacher", devotionalCourses: [], shraddhakutirId: 1, createdAt: new Date(), updatedAt: new Date() }
    ];

    this.nextId = 8;
  }

  // Devotees
  async getDevotees(page = 1, size = 10, filters: any = {}): Promise<{ data: Devotee[], total: number }> {
    let filtered = [...this.devotees];
    
    if (filters.search) {
      const search = filters.search.toLowerCase();
      filtered = filtered.filter(d => 
        d.legalName?.toLowerCase().includes(search) ||
        d.name?.toLowerCase().includes(search) ||
        d.email?.toLowerCase().includes(search)
      );
    }
    
    if (filters.namhattaId) {
      filtered = filtered.filter(d => d.namhattaId === parseInt(filters.namhattaId));
    }
    
    const total = filtered.length;
    const offset = (page - 1) * size;
    const data = filtered.slice(offset, offset + size);
    
    return { data, total };
  }

  async getDevotee(id: number): Promise<Devotee | undefined> {
    return this.devotees.find(d => d.id === id);
  }

  async createDevotee(devotee: InsertDevotee): Promise<Devotee> {
    const newDevotee: Devotee = {
      ...devotee,
      id: this.nextId++,
      createdAt: new Date(),
      updatedAt: new Date()
    };
    this.devotees.push(newDevotee);
    return newDevotee;
  }

  async createDevoteeForNamhatta(devotee: InsertDevotee, namhattaId: number): Promise<Devotee> {
    return this.createDevotee({ ...devotee, namhattaId });
  }

  async updateDevotee(id: number, devotee: Partial<InsertDevotee>): Promise<Devotee> {
    const index = this.devotees.findIndex(d => d.id === id);
    if (index === -1) throw new Error("Devotee not found");
    
    this.devotees[index] = { 
      ...this.devotees[index], 
      ...devotee, 
      updatedAt: new Date() 
    };
    return this.devotees[index];
  }

  async getDevoteesByNamhatta(namhattaId: number, page = 1, size = 10, statusId?: number): Promise<{ data: Devotee[], total: number }> {
    let filtered = this.devotees.filter(d => d.namhattaId === namhattaId);
    
    if (statusId) {
      filtered = filtered.filter(d => d.devotionalStatusId === statusId);
    }
    
    const total = filtered.length;
    const offset = (page - 1) * size;
    const data = filtered.slice(offset, offset + size);
    
    return { data, total };
  }

  async upgradeDevoteeStatus(id: number, newStatusId: number, notes?: string): Promise<void> {
    const devotee = this.devotees.find(d => d.id === id);
    if (!devotee) throw new Error("Devotee not found");
    
    const history: StatusHistory = {
      id: this.nextId++,
      devoteeId: id,
      previousStatus: devotee.devotionalStatusId?.toString(),
      newStatus: newStatusId.toString(),
      updatedAt: new Date(),
      comment: notes
    };
    
    this.statusHistory.push(history);
    devotee.devotionalStatusId = newStatusId;
    devotee.updatedAt = new Date();
  }

  async getDevoteeStatusHistory(id: number): Promise<StatusHistory[]> {
    return this.statusHistory.filter(h => h.devoteeId === id);
  }

  // Namhattas
  async getNamhattas(page = 1, size = 10, filters: any = {}): Promise<{ data: Namhatta[], total: number }> {
    let filtered = [...this.namhattas];
    
    if (filters.search) {
      const search = filters.search.toLowerCase();
      filtered = filtered.filter(n => 
        n.name?.toLowerCase().includes(search) ||
        n.code?.toLowerCase().includes(search)
      );
    }
    
    if (filters.status) {
      filtered = filtered.filter(n => n.status === filters.status);
    }
    
    const total = filtered.length;
    const offset = (page - 1) * size;
    const data = filtered.slice(offset, offset + size);
    
    return { data, total };
  }

  async getNamhatta(id: number): Promise<Namhatta | undefined> {
    return this.namhattas.find(n => n.id === id);
  }

  async createNamhatta(namhatta: InsertNamhatta): Promise<Namhatta> {
    const newNamhatta: Namhatta = {
      ...namhatta,
      id: this.nextId++,
      createdAt: new Date(),
      updatedAt: new Date()
    };
    this.namhattas.push(newNamhatta);
    return newNamhatta;
  }

  async updateNamhatta(id: number, namhatta: Partial<InsertNamhatta>): Promise<Namhatta> {
    const index = this.namhattas.findIndex(n => n.id === id);
    if (index === -1) throw new Error("Namhatta not found");
    
    this.namhattas[index] = { 
      ...this.namhattas[index], 
      ...namhatta, 
      updatedAt: new Date() 
    };
    return this.namhattas[index];
  }

  async approveNamhatta(id: number): Promise<void> {
    const namhatta = this.namhattas.find(n => n.id === id);
    if (!namhatta) throw new Error("Namhatta not found");
    
    namhatta.status = "APPROVED";
    namhatta.updatedAt = new Date();
  }

  async rejectNamhatta(id: number, reason?: string): Promise<void> {
    const namhatta = this.namhattas.find(n => n.id === id);
    if (!namhatta) throw new Error("Namhatta not found");
    
    namhatta.status = "REJECTED";
    namhatta.updatedAt = new Date();
  }

  async getNamhattaUpdates(id: number): Promise<NamhattaUpdate[]> {
    return this.namhattaUpdates.filter(u => u.namhattaId === id);
  }

  async getNamhattaDevoteeStatusCount(id: number): Promise<Record<string, number>> {
    const devotees = this.devotees.filter(d => d.namhattaId === id);
    const counts: Record<string, number> = {};
    
    for (const devotee of devotees) {
      if (devotee.devotionalStatusId) {
        const status = this.devotionalStatuses.find(s => s.id === devotee.devotionalStatusId);
        if (status) {
          counts[status.name] = (counts[status.name] || 0) + 1;
        }
      }
    }
    
    return counts;
  }

  async getNamhattaStatusHistory(id: number, page = 1, size = 10): Promise<{ data: StatusHistory[], total: number }> {
    const devotees = this.devotees.filter(d => d.namhattaId === id);
    const devoteeIds = devotees.map(d => d.id);
    const filtered = this.statusHistory.filter(h => devoteeIds.includes(h.devoteeId));
    
    const total = filtered.length;
    const offset = (page - 1) * size;
    const data = filtered.slice(offset, offset + size);
    
    return { data, total };
  }

  // Statuses
  async getDevotionalStatuses(): Promise<DevotionalStatus[]> {
    return [...this.devotionalStatuses];
  }

  async createDevotionalStatus(status: InsertDevotionalStatus): Promise<DevotionalStatus> {
    const newStatus: DevotionalStatus = {
      ...status,
      id: this.nextId++,
      createdAt: new Date()
    };
    this.devotionalStatuses.push(newStatus);
    return newStatus;
  }

  async renameDevotionalStatus(id: number, newName: string): Promise<void> {
    const status = this.devotionalStatuses.find(s => s.id === id);
    if (!status) throw new Error("Status not found");
    
    status.name = newName;
  }

  // Shraddhakutirs
  async getShraddhakutirs(district?: string): Promise<Shraddhakutir[]> {
    if (district) {
      return this.shraddhakutirs.filter(s => s.districtCode === district);
    }
    return [...this.shraddhakutirs];
  }

  async createShraddhakutir(shraddhakutir: InsertShraddhakutir): Promise<Shraddhakutir> {
    const newShraddhakutir: Shraddhakutir = {
      ...shraddhakutir,
      id: this.nextId++,
      createdAt: new Date()
    };
    this.shraddhakutirs.push(newShraddhakutir);
    return newShraddhakutir;
  }

  // Updates
  async createNamhattaUpdate(update: InsertNamhattaUpdate): Promise<NamhattaUpdate> {
    const newUpdate: NamhattaUpdate = {
      ...update,
      id: this.nextId++,
      createdAt: new Date()
    };
    this.namhattaUpdates.push(newUpdate);
    return newUpdate;
  }

  async getAllUpdates(): Promise<Array<NamhattaUpdate & { namhattaName: string }>> {
    return this.namhattaUpdates.map(update => {
      const namhatta = this.namhattas.find(n => n.id === update.namhattaId);
      return {
        ...update,
        namhattaName: namhatta?.name || "Unknown"
      };
    });
  }

  // Hierarchy
  async getTopLevelHierarchy(): Promise<{
    founder: Leader[];
    gbc: Leader[];
    regionalDirectors: Leader[];
    coRegionalDirectors: Leader[];
  }> {
    return {
      founder: this.leaders.filter(l => l.role === "FOUNDER_ACHARYA"),
      gbc: this.leaders.filter(l => l.role === "GBC"),
      regionalDirectors: this.leaders.filter(l => l.role === "REGIONAL_DIRECTOR"),
      coRegionalDirectors: this.leaders.filter(l => l.role === "CO_REGIONAL_DIRECTOR")
    };
  }

  async getLeadersByLevel(level: string): Promise<Leader[]> {
    return this.leaders.filter(l => l.role === level);
  }

  // Dashboard
  async getDashboardSummary(): Promise<{
    totalDevotees: number;
    totalNamhattas: number;
    recentUpdates: Array<{
      namhattaId: number;
      namhattaName: string;
      programType: string;
      date: string;
      attendance: number;
    }>;
  }> {
    const recentUpdates = this.namhattaUpdates
      .slice(-5)
      .map(update => {
        const namhatta = this.namhattas.find(n => n.id === update.namhattaId);
        return {
          namhattaId: update.namhattaId,
          namhattaName: namhatta?.name || "Unknown",
          programType: update.programType,
          date: update.date,
          attendance: update.attendance
        };
      });

    return {
      totalDevotees: this.devotees.length,
      totalNamhattas: this.namhattas.length,
      recentUpdates
    };
  }

  async getStatusDistribution(): Promise<Array<{
    statusName: string;
    count: number;
    percentage: number;
  }>> {
    const statusCounts: Record<string, number> = {};
    const total = this.devotees.length;

    for (const devotee of this.devotees) {
      if (devotee.devotionalStatusId) {
        const status = this.devotionalStatuses.find(s => s.id === devotee.devotionalStatusId);
        if (status) {
          statusCounts[status.name] = (statusCounts[status.name] || 0) + 1;
        }
      }
    }

    return Object.entries(statusCounts).map(([statusName, count]) => ({
      statusName,
      count,
      percentage: total > 0 ? (count / total) * 100 : 0
    }));
  }

  // Geography - Simple mock data for migration
  async getCountries(): Promise<string[]> {
    return ["India"];
  }

  async getStates(country?: string): Promise<string[]> {
    return ["West Bengal", "Odisha", "Bihar", "Jharkhand"];
  }

  async getDistricts(state?: string): Promise<string[]> {
    const districts = {
      "West Bengal": ["Nadia", "Kolkata", "Howrah", "24 Parganas North"],
      "Odisha": ["Khordha", "Cuttack", "Puri"],
      "Bihar": ["Patna", "Gaya", "Nalanda"],
      "Jharkhand": ["Ranchi", "Dhanbad", "Jamshedpur"]
    };
    return state ? (districts[state as keyof typeof districts] || []) : Object.values(districts).flat();
  }

  async getSubDistricts(district?: string, pincode?: string): Promise<string[]> {
    return ["Sub District 1", "Sub District 2"];
  }

  async getVillages(subDistrict?: string, pincode?: string): Promise<string[]> {
    return ["Village 1", "Village 2"];
  }

  async getPincodes(village?: string, district?: string, subDistrict?: string): Promise<string[]> {
    return ["700001", "700002", "741302"];
  }

  async searchPincodes(country: string, searchTerm: string, page: number, limit: number): Promise<{
    pincodes: string[];
    total: number;
    hasMore: boolean;
  }> {
    const allPincodes = ["700001", "700002", "741302", "751001", "800001"];
    const filtered = allPincodes.filter(p => p.includes(searchTerm));
    const offset = (page - 1) * limit;
    const pincodes = filtered.slice(offset, offset + limit);
    
    return {
      pincodes,
      total: filtered.length,
      hasMore: offset + limit < filtered.length
    };
  }

  async getAddressByPincode(pincode: string): Promise<{
    country: string;
    state: string;
    district: string;
    subDistricts: string[];
    villages: string[];
  } | null> {
    const pincodeMap: Record<string, any> = {
      "700001": {
        country: "India",
        state: "West Bengal",
        district: "Kolkata",
        subDistricts: ["Central Kolkata"],
        villages: ["Park Street", "Esplanade"]
      },
      "741302": {
        country: "India",
        state: "West Bengal",
        district: "Nadia",
        subDistricts: ["Mayapur"],
        villages: ["Mayapur", "Antardwip"]
      }
    };
    
    return pincodeMap[pincode] || null;
  }

  // Map data
  async getNamhattaCountsByCountry(): Promise<Array<{ country: string; count: number }>> {
    return [{ country: "India", count: this.namhattas.length }];
  }

  async getNamhattaCountsByState(country?: string): Promise<Array<{ state: string; country: string; count: number }>> {
    return [
      { state: "West Bengal", country: "India", count: 2 }
    ];
  }

  async getNamhattaCountsByDistrict(state?: string): Promise<Array<{ district: string; state: string; country: string; count: number }>> {
    return [
      { district: "Nadia", state: "West Bengal", country: "India", count: 1 },
      { district: "Kolkata", state: "West Bengal", country: "India", count: 1 }
    ];
  }

  async getNamhattaCountsBySubDistrict(district?: string): Promise<Array<{ subDistrict: string; district: string; state: string; country: string; count: number }>> {
    return [
      { subDistrict: "Mayapur", district: "Nadia", state: "West Bengal", country: "India", count: 1 }
    ];
  }

  async getNamhattaCountsByVillage(subDistrict?: string): Promise<Array<{ village: string; subDistrict: string; district: string; state: string; country: string; count: number }>> {
    return [
      { village: "Mayapur", subDistrict: "Mayapur", district: "Nadia", state: "West Bengal", country: "India", count: 1 }
    ];
  }
}
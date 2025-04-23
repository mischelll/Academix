import {create} from 'zustand';


export type Role = {
    name: string,
    description: string
};

export type User = {
    id: number,
    email: string,
    username: string
    avatar?: string,
    isVerified: boolean,
    firstName: string,
    phone: string,
    city?: string,
    roles: Role[] 
};

type UserState = {
    user: User | null,
    setUser: (user: User) => void,
    clearUser: () => void
};

export const useUserStore = create<UserState>((set) => ({
    user: null,
    setUser: (user) => set({user}),
    clearUser: () => set({user: null}),
}));
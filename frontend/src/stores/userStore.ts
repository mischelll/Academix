import {create} from 'zustand';

type User = {
    id: number,
    email: string,
    username: string
    avatar?: string,
    isVerified: boolean,
    firstName: string,
    phone: string,
    city?: string,
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
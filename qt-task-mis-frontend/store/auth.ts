import { IUser } from '@/types/schema'
import { create } from 'zustand'
import { devtools, persist } from 'zustand/middleware'
import type {} from '@redux-devtools/extension'

interface AuthStoreState {
    user: IUser | null
    setUser: (user:IUser) => void
  }

  
export  const useAuthStore = create<AuthStoreState>()(
    devtools(
      persist(
        (set) => ({
          user: null,
          setUser: (user) => set((state) => ({ user:user })),
        }),
        {
          name: 'auth-storage',
        },
      ),
    ),
  )

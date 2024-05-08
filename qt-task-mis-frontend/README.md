
# QT PRACTICAL TEST USER-TASK MANAGEMENT FRONTEND

A user and task management frontend application

## Get Started
-  Clone the repo
- run `npm install`
- run `npm dev`
- go to `http://localhost:3000/`

## app stacture

```bash
.2
├── README.md
├── app # pages directory
│   ├── dashboard
│   │   ├── layout.tsx
│   │   ├── tasks
│   │   └── users
│   ├── layout.tsx
│   └── page.tsx
├── components
│   ├── ...components.tsx
│   ├── ui # reusable components
│   │   ├── ...reusable-components.tsx
├── components.json
├── config #configurations
│   └── site.ts
├── lib # reusable utilities
│   ├── data.ts
│   ├── fonts.ts
│   └── utils.ts
├── next-env.d.ts
├── next.config.mjs
├── package-lock.json
├── package.json
├── postcss.config.js
├── prettier.config.js
├── public
├── services # services to handle backend calls
│   ├── projects
│   ├── tasks
│   └── users
│       ├── index.ts
│       ├── mutations.ts
│       └── queries.ts
├── store # state management
│   └── auth.ts
├── styles # styles
│   └── globals.css
├── tailwind.config.js
├── tsconfig.json
├── tsconfig.tsbuildinfo
└── types # all types
    ├── nav.ts
    └── schema.ts

```

## Technologies

- [Next.js](https://nextjs.org/) - React framework :heart:
- [Tailwind CSS](https://tailwindcss.com/) - CSS framework
- [React Query](https://react-query.tanstack.com/) - Data fetching and Caching
- [Zod](https://zod.dev/) - Data validation
- [Zustand](https://zustand.surge.sh/) - State management
- [Axios](https://axios-http.com/) - HTTP client (mostly I used fetch for making things faster)
- [Date-fns](https://date-fns.org/) - Date utility library
- [ShadCn-UI](https://ui.shadcn.com/) - UI components
- [Lucide](https://lucide.dev/) - Icons
- [React Day Picker](https://react-day-picker.js.org/) - Date picker


## Features

- [x] Authentication
- [x] Authorization
- [x] User CRUD operations
- [x] Task CRUD operations
- [X] Project CRUD operations

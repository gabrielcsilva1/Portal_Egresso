import { Route, Routes } from 'react-router'
import { AppLayout } from './pages/_layouts/app-layout'
import { AuthLayout } from './pages/_layouts/auth-layout'
import { CoordinatorConfigLayout } from './pages/_layouts/coordinator-config-layout'
import { GraduateConfigLayout } from './pages/_layouts/graduate-config-layout'
import { ManageCourses } from './pages/admin/manage-courses'
import { ManageGraduates } from './pages/admin/manage-graduates'
import { ManageJobOpportunities } from './pages/admin/manage-job-opportunities'
import { ManageTestimonials } from './pages/admin/manage-testimonials'
import { SignInAdmin } from './pages/admin/sign-in-admin'
import { Graduates } from './pages/graduate/list'
import { ManageGraduateJobOpportunity } from './pages/graduate/manage/manage-graduate-job-opportunity'
import { ManageGraduatePosition } from './pages/graduate/manage/manage-graduate-position'
import { ManageGraduateProfile } from './pages/graduate/manage/manage-graduate-profile'
import { ManageGraduateTestimonial } from './pages/graduate/manage/manage-graduate-testimonial'
import { Home } from './pages/home'
import { JobOpportunity } from './pages/job-opportunity'
import { Profile } from './pages/profile'
import { SignIn } from './pages/sign-in'
import { SignUp } from './pages/sign-up'

export function AppRoutes() {
  return (
    <Routes>
      <Route element={<AppLayout />}>
        <Route index element={<Home />} />
        <Route path='/graduate/:graduateId' element={<Profile />} />
        <Route path='/graduate' element={<Graduates />} />
        <Route path='/job-opportunity' element={<JobOpportunity />} />

        <Route element={<CoordinatorConfigLayout />}>
          <Route path='/manage/testimonial' element={<ManageTestimonials />} />
          <Route path='/manage/graduate' element={<ManageGraduates />} />
          <Route path='/manage/course' element={<ManageCourses />} />
          <Route
            path='/manage/job-opportunity'
            element={<ManageJobOpportunities />}
          />
        </Route>

        <Route path='/graduate' element={<GraduateConfigLayout />}>
          <Route path='manage/profile' element={<ManageGraduateProfile />} />
          <Route
            path='manage/testimonial'
            element={<ManageGraduateTestimonial />}
          />
          <Route
            path='manage/job-opportunity'
            element={<ManageGraduateJobOpportunity />}
          />
          <Route path='manage/position' element={<ManageGraduatePosition />} />
        </Route>
      </Route>

      <Route element={<AuthLayout />}>
        <Route path='/sign-in' element={<SignIn />} />
        <Route path='/admin/sign-in' element={<SignInAdmin />} />
        <Route path='/sign-up' element={<SignUp />} />
      </Route>
    </Routes>
  )
}

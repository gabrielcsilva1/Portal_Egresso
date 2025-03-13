export type PaginatedResponse<T> = {
  content: Array<T>
  pageIndex: number
  totalPages: number
  totalElements: number
}

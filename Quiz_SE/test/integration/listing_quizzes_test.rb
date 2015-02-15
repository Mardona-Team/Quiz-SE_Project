require 'test_helper'

class ListingQuizzesTest < ActionDispatch::IntegrationTest

	test 'returns list of all quizzes' do
		get '/api/quizzes'
		assert_equal 200, response.status
		refute_empty response.body
	end
end
package com.db.desafiovotacao.api.service;

import com.db.desafiovotacao.api.converters.VoteRecordConverter;
import com.db.desafiovotacao.api.entity.Agenda;
import com.db.desafiovotacao.api.entity.Member;
import com.db.desafiovotacao.api.entity.Vote;
import com.db.desafiovotacao.api.exception.AgendaNotFoundException;
import com.db.desafiovotacao.api.exception.MemberNotFoundException;
import com.db.desafiovotacao.api.exception.OperationNotPermittedException;
import com.db.desafiovotacao.api.record.VoteAgendaRecord;
import com.db.desafiovotacao.api.record.VoteMemberRecord;
import com.db.desafiovotacao.api.record.VoteRecord;
import com.db.desafiovotacao.api.repository.AgendaRepository;
import com.db.desafiovotacao.api.repository.MemberRepository;
import com.db.desafiovotacao.api.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VoteService implements VoteServiceInterface{
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private VoteRecordConverter voteRecordConverter;

    public VoteRecord vote(UUID memberId, UUID agendaId, Boolean voted) throws MemberNotFoundException, AgendaNotFoundException, OperationNotPermittedException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found"));

        Agenda agenda = agendaRepository.findById(agendaId)
                .orElseThrow(() -> new AgendaNotFoundException("Agenda not found"));

        if (voteRepository.existsByMemberAndAgenda(member, agenda)) {
            throw new OperationNotPermittedException("Member has already voted on this agenda");
        }


        Vote newVote = new Vote();
        newVote.setMember(member);
        newVote.setAgenda(agenda);
        newVote.setVoted(voted);
        newVote=voteRepository.save(newVote);

        return voteRecordConverter.toVoteRecord(newVote);
    }
}
